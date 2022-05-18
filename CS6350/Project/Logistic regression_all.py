# Databricks notebook source
# MAGIC %md
# MAGIC ### Logistic Regression using Pyspark
# MAGIC This is a notebook implementing the Multiclass Logistic Regression model using numpy and pyspark.<br>
# MAGIC Each datapoint calculation (gradient, loss and prediction) is done using the map and reduce function in pysprak<br>

# COMMAND ----------

!pip install nltk

load_previous = False

import string
import nltk
from nltk.corpus import stopwords
from pyspark import SparkFiles

# COMMAND ----------

# MAGIC %md
# MAGIC ### Preprocess

# COMMAND ----------

STOP_WORDS = set(
    """
a about above across after afterwards again against all almost alone along
already also although always am among amongst amount an and another any anyhow
anyone anything anyway anywhere are around as at
back be became because become becomes becoming been before beforehand behind
being below beside besides between beyond both bottom but by
call can cannot ca could
did do does doing done down due during
each eight either eleven else elsewhere empty enough even ever every
everyone everything everywhere except
few fifteen fifty first five for former formerly forty four from front full
further
get give go
had has have he hence her here hereafter hereby herein hereupon hers herself
him himself his how however hundred
i if in indeed into is it its itself
keep
last latter latterly least less
just
made make many may me meanwhile might mine more moreover most mostly move much
must my myself
name namely neither never nevertheless next nine no nobody none noone nor not
nothing now nowhere
of off often on once one only onto or other others otherwise our ours ourselves
out over own
part per perhaps please put
quite
rather re really regarding
same say see seem seemed seeming seems serious several she should show side
since six sixty so some somehow someone something sometime sometimes somewhere
still such
take ten than that the their them themselves then thence there thereafter
thereby therefore therein thereupon these they third this those though three
through throughout thru thus to together too top toward towards twelve twenty
two
under until up unless upon us used using
various very very via was we well were what whatever when whence whenever where
whereafter whereas whereby wherein whereupon wherever whether which while
whither who whoever whole whom whose why will with within without would
yet you your yours yourself yourselves
""".split()
)


def preprocessing_tweets(tweet) :
# 	print("TWEET ", tweet)
	tweet= tweet.lower()
	tweet=tweet.strip()

	arr= tweet.split(" ")

	#remove url
	cleantokens= []
	for el in arr:
		if(not 'http' in el):
			el= el.strip()
			cleantokens.append(el)


	#remove usernames, keep after hashtag
	cleantokens2= []
	for el in cleantokens:
		if(el.find("@") != 0 and not el.find("#") == 0): #regular words, not hashtags
			cleantokens2.append(el)
		if(el.find("#") == 0):
# 			print(el)
			cleantokens2.append(el[1:])

# 	print("TWEET after removing", cleantokens2)

	punct_list = list(string.punctuation)
# 	print("PUNCT", string.punctuation)
	for el in cleantokens2:
		#remove punc
		for punc in punct_list:
			if punc in el:
				el = el.replace(punc, ' ')
			el= el.strip()

# 	print("TWEET after removing", cleantokens2)

	#two options: exists, counts in map

	#remove stopwords
	cleantokens3= []
	for el in cleantokens2:
		if el not in STOP_WORDS:
			cleantokens3.append(el)

# 	print("TWEET after removing", cleantokens3)
	#first 1 gram then 2 gram

	
	return " ".join(cleantokens3)


tweet= " @JReebo: Who wants to get there nose in these bad bois then #scally #chav #sockfetish #stinking http://t.co/FeQxgN0W6I hot sox and legs"
print(preprocessing_tweets(tweet))

# COMMAND ----------

import nltk
nltk.download('punkt')
nltk.download('stopwords')



import math
from collections import Counter
import numpy as np
from scipy.special import softmax
from nltk import word_tokenize
from nltk.corpus import stopwords
stop_words=set(stopwords.words('english'))



# put 1 where the value is maximum
def onehot(x):
    b = (x == np.max(x))
    return b.astype(int)



folder = "/FileStore/tables/log_reg_weighted_loss_v4"
dbutils.fs.mkdirs(folder)



def read_file(filename):
    return dbutils.fs.head(f"{folder}/{filename}", 100000000)

def write_to_file(s, filename, overwrite=True):
    dbutils.fs.put(f"{folder}/{filename}", s, overwrite=overwrite)

def load_idf_dict():
    return {line.split()[0]: float(line.split()[1]) for line in read_file("idf_dict.txt").splitlines()}

def write_idf_dict(idf_dict):
    idf_string = "\n".join([f"{k}\t{v}" for k, v in idf_dict.items()])
    write_to_file(idf_string, "idf_dict.txt")

def load_word_list():
    return read_file("vocab.txt").splitlines()

def write_word_list(word_index_dict):
    write_to_file(s= "\n".join(word_index_dict.keys()), filename="vocab.txt")


def load_weights():
    weight_text = read_file("weights.txt")
    return np.array(
        [[float(y) for y in x.split("\t")] for x in weight_text.splitlines()]
    )

def write_weights(W, file=None):
    if file is None:
        file = "weights.txt"

    write_to_file("\n".join(["\t".join([str(y) for y in x]) for x in W]), file) 


def write_loss(loss_list):
    s= "\n".join([f"epoch {i} loss {ls}"  for i, ls in enumerate(loss_list)])
    write_to_file(s, "loss.txt")

# COMMAND ----------

# MAGIC %md
# MAGIC ### Load Data and featurize

# COMMAND ----------

path = "https://raw.githubusercontent.com/mandalbiswadip/data_storage/main/labeled_data.csv"
spark.sparkContext.addFile(path)
df = spark.read.option("header","true").option("inferSchema","true").csv("file://"+ SparkFiles.get("labeled_data.csv"))
df = df.where(df.tweet.isNotNull() )

column_names = df.columns
print("columns: ", column_names)
# Preprocess
rdd2=df.rdd.map(lambda x: 
    (x[0], x[1], x[2], x[3], x[4], x[5], preprocessing_tweets(x[6]))
    )  
df=rdd2.toDF(column_names)

# split. setting seed to reproduce results in multiple run
df, test_df = df.randomSplit(weights=[0.8, 0.2], seed=12345)

N = df.rdd.count()

# GET IDF DICT

if load_previous:
    print("Loading previous word list and idf dict")
    idf_dict = load_idf_dict()
    unique_words = load_word_list()

else:
    print("Calculating IDF scores")

    data_rdd = df.select("_c0", "tweet").rdd
    data_tokenized = data_rdd.map( lambda x : ( x[0], word_tokenize(x[1].lower())))
    data_tokenized =  data_tokenized.map(
        lambda x: (x[0], [word for word in x[1] if word not in stop_words]) 
    )
    # INVERSE INDEXING

    # get frequency of words in each docuement
    flat_tokenized_data =  data_tokenized.flatMap(
    lambda x: [((word, x[0]), 1) for word in x[1]]
    )
    flat_tokenized_data_v2 = flat_tokenized_data.reduceByKey(
        lambda a,b: a+b
    ).map(
        lambda x: (x[0][0], [(x[0][1], x[1])])
    )
    unique_words = data_tokenized.flatMap(lambda x : x[1]).distinct().collect()
    flat_tokenized_data_reduced = flat_tokenized_data_v2.reduceByKey(lambda a,b: a+b )
    # calculating idf
    flat_tokenized_data_idf_reduced = flat_tokenized_data_reduced.map(
        lambda x : (x[0], math.log( N / len(x[1])) ) 
    )
    idf_dict = dict(flat_tokenized_data_idf_reduced.collect())

    write_idf_dict(idf_dict)

print("unique words(showing only 10): ", unique_words[:10])

word_index_dict = dict()
for index, word in enumerate(unique_words):
    word_index_dict[word] = index

if not load_previous:
    write_word_list(word_index_dict)


def get_tfidf(sentence):
    words = word_tokenize(sentence)
    vec = np.zeros(len(unique_words) + 1)
    vec[-1] = 1 # this is for the bias term

    for word, v in Counter(words).items():
        if word in word_index_dict:
            vec[word_index_dict[word]] = v * idf_dict[word]
    return vec


text_rdd = df.select("tweet").rdd
tfidf_rdd = text_rdd.map(lambda x : get_tfidf(x[0]))
# tfidf_rdd.collect()
X_rdd = tfidf_rdd
class_rdd = df.select("class").rdd
class_rdd = class_rdd.map(lambda x : x[0])

def get_class_vector(class_name):
    vec = np.zeros(3, dtype=int)
    vec[class_name] = 1
    return vec

Y = np.zeros((class_rdd.count(), class_rdd.distinct().count())) 
class_vec_rdd = class_rdd.map(lambda x : get_class_vector(x))
Y_rdd = class_vec_rdd

print("class vectors: ", Y_rdd.take(4))


data = X_rdd.zip(Y_rdd)
feature_count = X_rdd.map(lambda x : len(x)).take(1)[0]
class_count = Y_rdd.map(lambda x : len(x)).take(1)[0]
print("Feature count: ", feature_count)

# COMMAND ----------

# MAGIC %md
# MAGIC ### Define or load weights

# COMMAND ----------

if load_previous:
    W = load_weights()
else:
    # initialize weights randomly
    W = np.random.normal(0, .1, (feature_count, class_count))
print("Weight matrix shape", W.shape)

# COMMAND ----------


# defining gradient and loss for a single datapoint
# To be used inside map function of rdd
def gradient(x, y):
    P = softmax(- x @ W, axis=-1)
    grad = np.expand_dims(x, 1) @ (y - np.expand_dims(P, 0))
    return grad

def loss(x, y):
    return  (x @ (W @ y) + np.log(np.sum( np.exp(- x @ W))))


lr = .1
decay_rate = .99
def add(a: np.ndarray, b: np.ndarray) -> np.ndarray:
    return np.add(a, b)
assert N == data.count(), "Inconsistency in data shape"
N = data.count()


# COMMAND ----------

# MAGIC %md
# MAGIC ### Gradient Descent

# COMMAND ----------

loss_list = []
for i in range(220, 1000):
    W = W - lr / N * data.map(lambda x : gradient(x[0],x[1])).reduce(add)
#     lr = lr * decay_rate** ((i+1) / 100  )
    tot_loss = data.map(lambda x : loss(x[0], x[1])).reduce(add)
    loss_list.append(tot_loss / N)
    print(f"epoch {i} loss {tot_loss / N}")
    
    
    if i % 10 == 0:
        write_weights(W)
#         write_loss(loss_list)

# COMMAND ----------

# MAGIC %md
# MAGIC ### Prediction and Accuracy

# COMMAND ----------

from sklearn.metrics import confusion_matrix, classification_report

# COMMAND ----------

# MAGIC %md
# MAGIC ### On Train Data

# COMMAND ----------

predict = lambda x: np.argmax(softmax(- x @ W))
label_data = data.map(lambda x : (np.argmax(x[1]), predict(x[0])))
# Accuracy
print("accuracy ", label_data.map(lambda x : int(x[0] == x[1])).reduce(add) / N)





train_true = label_data.map(lambda x: x[0]).collect()
train_predict = label_data.map(lambda x: x[1]).collect()
print(confusion_matrix(train_true, train_predict ))
print(classification_report(train_true, train_predict))

# COMMAND ----------

# MAGIC %md
# MAGIC ### On Test Data

# COMMAND ----------

test_text_rdd = test_df.select("tweet").rdd
test_tfidf_rdd = test_text_rdd.map(lambda x : get_tfidf(x[0]))
# tfidf_rdd.collect()
test_X_rdd = test_tfidf_rdd
test_class_rdd = test_df.select("class").rdd
test_class_rdd = test_class_rdd.map(lambda x : x[0])

# COMMAND ----------

test_data = test_tfidf_rdd.zip(test_class_rdd)
test_N = test_data.count()
test_N

# COMMAND ----------

test_label_data = test_data.map(lambda x : (x[1], predict(x[0])))
# Accuracy
print("accuracy ", test_label_data
      .map(lambda x : int(x[0] == x[1])).reduce(add) / test_N)

test_true = test_label_data.map(lambda x: x[0]).collect()
test_label = test_label_data.map(lambda x: x[1]).collect()
print(confusion_matrix(test_true, test_label ))
print(classification_report(test_true, test_label ))

# COMMAND ----------










# Databricks notebook source
# MAGIC %md
# MAGIC ### Logistic Regression using Pyspark - with l2 regularization
# MAGIC This is a notebook implementing the Multiclass Logistic Regression model using numpy and pyspark.<br>
# MAGIC Additionally, l2-regularization has been used<br>
# MAGIC Each datapoint calculation (gradient, loss and prediction) is done using the map and reduce function in pysprak<br>

# COMMAND ----------

!pip install nltk


# MAGIC %md
# MAGIC ### Preprocess

load_previous = False

import string
import nltk
from nltk.corpus import stopwords
from pyspark import SparkFiles

# COMMAND ----------

# MAGIC %md
# MAGIC ### Preprocess

# COMMAND ----------

STOP_WORDS = set(
    """
a about above across after afterwards again against all almost alone along
already also although always am among amongst amount an and another any anyhow
anyone anything anyway anywhere are around as at
back be became because become becomes becoming been before beforehand behind
being below beside besides between beyond both bottom but by
call can cannot ca could
did do does doing done down due during
each eight either eleven else elsewhere empty enough even ever every
everyone everything everywhere except
few fifteen fifty first five for former formerly forty four from front full
further
get give go
had has have he hence her here hereafter hereby herein hereupon hers herself
him himself his how however hundred
i if in indeed into is it its itself
keep
last latter latterly least less
just
made make many may me meanwhile might mine more moreover most mostly move much
must my myself
name namely neither never nevertheless next nine no nobody none noone nor not
nothing now nowhere
of off often on once one only onto or other others otherwise our ours ourselves
out over own
part per perhaps please put
quite
rather re really regarding
same say see seem seemed seeming seems serious several she should show side
since six sixty so some somehow someone something sometime sometimes somewhere
still such
take ten than that the their them themselves then thence there thereafter
thereby therefore therein thereupon these they third this those though three
through throughout thru thus to together too top toward towards twelve twenty
two
under until up unless upon us used using
various very very via was we well were what whatever when whence whenever where
whereafter whereas whereby wherein whereupon wherever whether which while
whither who whoever whole whom whose why will with within without would
yet you your yours yourself yourselves
""".split()
)


def preprocessing_tweets(tweet) :
#   print("TWEET ", tweet)
    tweet= tweet.lower()
    tweet=tweet.strip()

    arr= tweet.split(" ")

    #remove url
    cleantokens= []
    for el in arr:
        if(not 'http' in el):
            el= el.strip()
            cleantokens.append(el)


    #remove usernames, keep after hashtag
    cleantokens2= []
    for el in cleantokens:
        if(el.find("@") != 0 and not el.find("#") == 0): #regular words, not hashtags
            cleantokens2.append(el)
        if(el.find("#") == 0):
#           print(el)
            cleantokens2.append(el[1:])

#   print("TWEET after removing", cleantokens2)

    punct_list = list(string.punctuation)
#   print("PUNCT", string.punctuation)
    for el in cleantokens2:
        #remove punc
        for punc in punct_list:
            if punc in el:
                el = el.replace(punc, ' ')
            el= el.strip()

#   print("TWEET after removing", cleantokens2)

    #two options: exists, counts in map

    #remove stopwords
    cleantokens3= []
    for el in cleantokens2:
        if el not in STOP_WORDS:
            cleantokens3.append(el)

#   print("TWEET after removing", cleantokens3)
    #first 1 gram then 2 gram

    
    return " ".join(cleantokens3)


tweet= " @JReebo: Who wants to get there nose in these bad bois then #scally #chav #sockfetish #stinking http://t.co/FeQxgN0W6I hot sox and legs"
print(preprocessing_tweets(tweet))

# COMMAND ----------

import nltk
nltk.download('punkt')
nltk.download('stopwords')



import math
from collections import Counter
import numpy as np
from scipy.special import softmax
from nltk import word_tokenize
from nltk.corpus import stopwords
stop_words=set(stopwords.words('english'))



# put 1 where the value is maximum
def onehot(x):
    b = (x == np.max(x))
    return b.astype(int)



folder = "/FileStore/tables/log_reg_regularization_v1"
dbutils.fs.mkdirs(folder)



def read_file(filename):
    return dbutils.fs.head(f"{folder}/{filename}", 100000000)

def write_to_file(s, filename, overwrite=True):
    dbutils.fs.put(f"{folder}/{filename}", s, overwrite=overwrite)

def load_idf_dict():
    return {line.split()[0]: float(line.split()[1]) for line in read_file("idf_dict.txt").splitlines()}

def write_idf_dict(idf_dict):
    idf_string = "\n".join([f"{k}\t{v}" for k, v in idf_dict.items()])
    write_to_file(idf_string, "idf_dict.txt")

def load_word_list():
    return read_file("vocab.txt").splitlines()

def write_word_list(word_index_dict):
    write_to_file(s= "\n".join(word_index_dict.keys()), filename="vocab.txt")


def load_weights():
    weight_text = read_file("weights.txt")
    return np.array(
        [[float(y) for y in x.split("\t")] for x in weight_text.splitlines()]
    )

def write_weights(W, file=None):
    if file is None:
        file = "weights.txt"

    write_to_file("\n".join(["\t".join([str(y) for y in x]) for x in W]), file) 


def write_loss(loss_list):
    s= "\n".join([f"epoch {i} loss {ls}"  for i, ls in enumerate(loss_list)])
    write_to_file(s, "loss.txt")

# COMMAND ----------

# MAGIC %md
# MAGIC ### Load Data and featurize

# COMMAND ----------

path = "https://raw.githubusercontent.com/mandalbiswadip/data_storage/main/labeled_data.csv"
spark.sparkContext.addFile(path)
df = spark.read.option("header","true").option("inferSchema","true").csv("file://"+ SparkFiles.get("labeled_data.csv"))
df = df.where(df.tweet.isNotNull() )


column_names = df.columns
print("columns: ", column_names)
# Preprocess
rdd2=df.rdd.map(lambda x: 
    (x[0], x[1], x[2], x[3], x[4], x[5], preprocessing_tweets(x[6]))
    )  
df=rdd2.toDF(column_names)

# split. setting seed to reproduce results in multiple run
df, test_df = df.randomSplit(weights=[0.8, 0.2], seed=12345)

N = df.rdd.count()

# GET IDF DICT

if load_previous:
    print("Loading previous word list and idf dict")
    idf_dict = load_idf_dict()
    unique_words = load_word_list()

else:
    print("Calculating IDF scores")

    data_rdd = df.select("_c0", "tweet").rdd
    data_tokenized = data_rdd.map( lambda x : ( x[0], word_tokenize(x[1].lower())))
    data_tokenized =  data_tokenized.map(
        lambda x: (x[0], [word for word in x[1] if word not in stop_words]) 
    )
    # INVERSE INDEXING

    # get frequency of words in each docuement
    flat_tokenized_data =  data_tokenized.flatMap(
    lambda x: [((word, x[0]), 1) for word in x[1]]
    )
    flat_tokenized_data_v2 = flat_tokenized_data.reduceByKey(
        lambda a,b: a+b
    ).map(
        lambda x: (x[0][0], [(x[0][1], x[1])])
    )
    unique_words = data_tokenized.flatMap(lambda x : x[1]).distinct().collect()
    flat_tokenized_data_reduced = flat_tokenized_data_v2.reduceByKey(lambda a,b: a+b )
    # calculating idf
    flat_tokenized_data_idf_reduced = flat_tokenized_data_reduced.map(
        lambda x : (x[0], math.log( N / len(x[1])) ) 
    )
    idf_dict = dict(flat_tokenized_data_idf_reduced.collect())

    write_idf_dict(idf_dict)

print("unique words(showing only 10): ", unique_words[:10])

word_index_dict = dict()
for index, word in enumerate(unique_words):
    word_index_dict[word] = index

if not load_previous:
    write_word_list(word_index_dict)


def get_tfidf(sentence):
    words = word_tokenize(sentence)
    vec = np.zeros(len(unique_words) + 1)
    vec[-1] = 1 # this is for the bias term

    for word, v in Counter(words).items():
        if word in word_index_dict:
            vec[word_index_dict[word]] = v * idf_dict[word]
    return vec


text_rdd = df.select("tweet").rdd
tfidf_rdd = text_rdd.map(lambda x : get_tfidf(x[0]))
# tfidf_rdd.collect()
X_rdd = tfidf_rdd
class_rdd = df.select("class").rdd
class_rdd = class_rdd.map(lambda x : x[0])

def get_class_vector(class_name):
    vec = np.zeros(3, dtype=int)
    vec[class_name] = 1
    return vec

Y = np.zeros((class_rdd.count(), class_rdd.distinct().count())) 
class_vec_rdd = class_rdd.map(lambda x : get_class_vector(x))
Y_rdd = class_vec_rdd

print("class vectors: ", Y_rdd.take(4))


data = X_rdd.zip(Y_rdd)
feature_count = X_rdd.map(lambda x : len(x)).take(1)[0]
class_count = Y_rdd.map(lambda x : len(x)).take(1)[0]
print("Feature count: ", feature_count)

# COMMAND ----------

# MAGIC %md
# MAGIC ### Define or load weights

# COMMAND ----------

if load_previous:
    W = load_weights()
else:
    # initialize weights randomly
    W = np.random.normal(0, .1, (feature_count, class_count))
print("Weight matrix shape", W.shape)

# defining gradient and loss for a single datapoint
# To be used inside map function of rdd
mu = 0.1
def gradient(x, y):
    P = softmax(- x @ W, axis=-1)
    grad = np.expand_dims(x, 1) @ (y - np.expand_dims(P, 0)) + 2 * mu * W
    return grad

def loss(x, y):
    return  x @ (W @ y) + np.log(np.sum( np.exp(- x @ W))) + mu * np.linalg.norm(W, ord=2)


# THE GRADIENT DESCENT STEP
lr = .3
decay_rate = .99
def add(a: np.ndarray, b: np.ndarray) -> np.ndarray:
    return np.add(a, b)
assert N == data.count(), "Inconsistency in data shape"
N = data.count()


# COMMAND ----------

# MAGIC %md
# MAGIC ### Gradient Descent

# COMMAND ----------

loss_list = []
lr = .1
for i in range(1000):
    W = W - lr / N * data.map(lambda x : gradient(x[0],x[1])).reduce(add)
#     lr = lr * decay_rate** ((i+1) / 100  )
    tot_loss = data.map(lambda x : loss(x[0], x[1])).reduce(add)
    loss_list.append(tot_loss / N)
    print(f"epoch {i} loss {tot_loss / N}")
    
    
    if i % 10 == 0:
        write_weights(W)
        write_loss(loss_list)

# COMMAND ----------

# MAGIC %md
# MAGIC ### Prediction and Accuracy

# COMMAND ----------

from sklearn.metrics import confusion_matrix, classification_report

# COMMAND ----------

# MAGIC %md
# MAGIC ### On Train Data

# COMMAND ----------


predict = lambda x: np.argmax(softmax(- x @ W))
label_data = data.map(lambda x : (np.argmax(x[1]), predict(x[0])))
# Accuracy
print("accuracy ", label_data.map(lambda x : int(x[0] == x[1])).reduce(add) / N)

# COMMAND ----------


train_true = label_data.map(lambda x: x[0]).collect()
train_predict = label_data.map(lambda x: x[1]).collect()
print(confusion_matrix(train_true, train_predict ))
print(classification_report(train_true, train_predict))

# COMMAND ----------

# MAGIC %md
# MAGIC ### On Test Data

# COMMAND ----------

test_text_rdd = test_df.select("tweet").rdd
test_tfidf_rdd = test_text_rdd.map(lambda x : get_tfidf(x[0]))
# tfidf_rdd.collect()
test_X_rdd = test_tfidf_rdd
test_class_rdd = test_df.select("class").rdd
test_class_rdd = test_class_rdd.map(lambda x : x[0])

# COMMAND ----------

test_data = test_tfidf_rdd.zip(test_class_rdd)
test_N = test_data.count()
test_N

# COMMAND ----------

test_label_data = test_data.map(lambda x : (x[1], predict(x[0])))
# Accuracy
print("accuracy ", test_label_data
      .map(lambda x : int(x[0] == x[1])).reduce(add) / test_N)

test_true = test_label_data.map(lambda x: x[0]).collect()
test_label = test_label_data.map(lambda x: x[1]).collect()
print(confusion_matrix(test_true, test_label ))
print(classification_report(test_true, test_label ))

# COMMAND ----------



# COMMAND ----------













# Databricks notebook source
# MAGIC %md
# MAGIC ### Logistic Regression using Pyspark - with l1 regularization
# MAGIC This is a notebook implementing the Multiclass Logistic Regression model using numpy and pyspark.<br>
# MAGIC Additionally, l1-regularization has been used<br>
# MAGIC Each datapoint calculation (gradient, loss and prediction) is done using the map and reduce function in pysprak<br>

# COMMAND ----------

!pip install nltk


# MAGIC %md
# MAGIC ### Preprocess

load_previous = False

import string
import nltk
from nltk.corpus import stopwords
from pyspark import SparkFiles

# COMMAND ----------

STOP_WORDS = set(
    """
a about above across after afterwards again against all almost alone along
already also although always am among amongst amount an and another any anyhow
anyone anything anyway anywhere are around as at
back be became because become becomes becoming been before beforehand behind
being below beside besides between beyond both bottom but by
call can cannot ca could
did do does doing done down due during
each eight either eleven else elsewhere empty enough even ever every
everyone everything everywhere except
few fifteen fifty first five for former formerly forty four from front full
further
get give go
had has have he hence her here hereafter hereby herein hereupon hers herself
him himself his how however hundred
i if in indeed into is it its itself
keep
last latter latterly least less
just
made make many may me meanwhile might mine more moreover most mostly move much
must my myself
name namely neither never nevertheless next nine no nobody none noone nor not
nothing now nowhere
of off often on once one only onto or other others otherwise our ours ourselves
out over own
part per perhaps please put
quite
rather re really regarding
same say see seem seemed seeming seems serious several she should show side
since six sixty so some somehow someone something sometime sometimes somewhere
still such
take ten than that the their them themselves then thence there thereafter
thereby therefore therein thereupon these they third this those though three
through throughout thru thus to together too top toward towards twelve twenty
two
under until up unless upon us used using
various very very via was we well were what whatever when whence whenever where
whereafter whereas whereby wherein whereupon wherever whether which while
whither who whoever whole whom whose why will with within without would
yet you your yours yourself yourselves
""".split()
)


def preprocessing_tweets(tweet) :
#   print("TWEET ", tweet)
    tweet= tweet.lower()
    tweet=tweet.strip()

    arr= tweet.split(" ")

    #remove url
    cleantokens= []
    for el in arr:
        if(not 'http' in el):
            el= el.strip()
            cleantokens.append(el)


    #remove usernames, keep after hashtag
    cleantokens2= []
    for el in cleantokens:
        if(el.find("@") != 0 and not el.find("#") == 0): #regular words, not hashtags
            cleantokens2.append(el)
        if(el.find("#") == 0):
#           print(el)
            cleantokens2.append(el[1:])

#   print("TWEET after removing", cleantokens2)

    punct_list = list(string.punctuation)
#   print("PUNCT", string.punctuation)
    for el in cleantokens2:
        #remove punc
        for punc in punct_list:
            if punc in el:
                el = el.replace(punc, ' ')
            el= el.strip()

#   print("TWEET after removing", cleantokens2)

    #two options: exists, counts in map

    #remove stopwords
    cleantokens3= []
    for el in cleantokens2:
        if el not in STOP_WORDS:
            cleantokens3.append(el)

#   print("TWEET after removing", cleantokens3)
    #first 1 gram then 2 gram

    
    return " ".join(cleantokens3)


tweet= " @JReebo: Who wants to get there nose in these bad bois then #scally #chav #sockfetish #stinking http://t.co/FeQxgN0W6I hot sox and legs"
print(preprocessing_tweets(tweet))

# COMMAND ----------

import nltk
nltk.download('punkt')
nltk.download('stopwords')



import math
from collections import Counter
import numpy as np
from scipy.special import softmax
from nltk import word_tokenize
from nltk.corpus import stopwords
stop_words=set(stopwords.words('english'))



# put 1 where the value is maximum
def onehot(x):
    b = (x == np.max(x))
    return b.astype(int)



folder = "/FileStore/tables/log_reg_regularization_l1_v1"
dbutils.fs.mkdirs(folder)



def read_file(filename):
    return dbutils.fs.head(f"{folder}/{filename}", 100000000)

def write_to_file(s, filename, overwrite=True):
    dbutils.fs.put(f"{folder}/{filename}", s, overwrite=overwrite)

def load_idf_dict():
    return {line.split()[0]: float(line.split()[1]) for line in read_file("idf_dict.txt").splitlines()}

def write_idf_dict(idf_dict):
    idf_string = "\n".join([f"{k}\t{v}" for k, v in idf_dict.items()])
    write_to_file(idf_string, "idf_dict.txt")

def load_word_list():
    return read_file("vocab.txt").splitlines()

def write_word_list(word_index_dict):
    write_to_file(s= "\n".join(word_index_dict.keys()), filename="vocab.txt")


def load_weights():
    weight_text = read_file("weights.txt")
    return np.array(
        [[float(y) for y in x.split("\t")] for x in weight_text.splitlines()]
    )

def write_weights(W, file=None):
    if file is None:
        file = "weights.txt"

    write_to_file("\n".join(["\t".join([str(y) for y in x]) for x in W]), file) 


def write_loss(loss_list):
    s= "\n".join([f"epoch {i} loss {ls}"  for i, ls in enumerate(loss_list)])
    write_to_file(s, "loss.txt")

# COMMAND ----------

path = "https://raw.githubusercontent.com/mandalbiswadip/data_storage/main/labeled_data.csv"
spark.sparkContext.addFile(path)
df = spark.read.option("header","true").option("inferSchema","true").csv("file://"+ SparkFiles.get("labeled_data.csv"))
df = df.where(df.tweet.isNotNull() )

column_names = df.columns
print("columns: ", column_names)
# Preprocess
rdd2=df.rdd.map(lambda x: 
    (x[0], x[1], x[2], x[3], x[4], x[5], preprocessing_tweets(x[6]))
    )  
df=rdd2.toDF(column_names)

# split. setting seed to reproduce results in multiple run
df, test_df = df.randomSplit(weights=[0.8, 0.2], seed=12345)

N = df.rdd.count()

# GET IDF DICT

if load_previous:
    print("Loading previous word list and idf dict")
    idf_dict = load_idf_dict()
    unique_words = load_word_list()

else:
    print("Calculating IDF scores")

    data_rdd = df.select("_c0", "tweet").rdd
    data_tokenized = data_rdd.map( lambda x : ( x[0], word_tokenize(x[1].lower())))
    data_tokenized =  data_tokenized.map(
        lambda x: (x[0], [word for word in x[1] if word not in stop_words]) 
    )
    # INVERSE INDEXING

    # get frequency of words in each docuement
    flat_tokenized_data =  data_tokenized.flatMap(
    lambda x: [((word, x[0]), 1) for word in x[1]]
    )
    flat_tokenized_data_v2 = flat_tokenized_data.reduceByKey(
        lambda a,b: a+b
    ).map(
        lambda x: (x[0][0], [(x[0][1], x[1])])
    )
    unique_words = data_tokenized.flatMap(lambda x : x[1]).distinct().collect()
    flat_tokenized_data_reduced = flat_tokenized_data_v2.reduceByKey(lambda a,b: a+b )
    # calculating idf
    flat_tokenized_data_idf_reduced = flat_tokenized_data_reduced.map(
        lambda x : (x[0], math.log( N / len(x[1])) ) 
    )
    idf_dict = dict(flat_tokenized_data_idf_reduced.collect())

    write_idf_dict(idf_dict)

print("unique words(showing only 10): ", unique_words[:10])

word_index_dict = dict()
for index, word in enumerate(unique_words):
    word_index_dict[word] = index

if not load_previous:
    write_word_list(word_index_dict)


def get_tfidf(sentence):
    words = word_tokenize(sentence)
    vec = np.zeros(len(unique_words) + 1)
    vec[-1] = 1 # this is for the bias term

    for word, v in Counter(words).items():
        if word in word_index_dict:
            vec[word_index_dict[word]] = v * idf_dict[word]
    return vec


text_rdd = df.select("tweet").rdd
tfidf_rdd = text_rdd.map(lambda x : get_tfidf(x[0]))
# tfidf_rdd.collect()
X_rdd = tfidf_rdd
class_rdd = df.select("class").rdd
class_rdd = class_rdd.map(lambda x : x[0])

def get_class_vector(class_name):
    vec = np.zeros(3, dtype=int)
    vec[class_name] = 1
    return vec

Y = np.zeros((class_rdd.count(), class_rdd.distinct().count())) 
class_vec_rdd = class_rdd.map(lambda x : get_class_vector(x))
Y_rdd = class_vec_rdd

print("class vectors: ", Y_rdd.take(4))


data = X_rdd.zip(Y_rdd)
feature_count = X_rdd.map(lambda x : len(x)).take(1)[0]
class_count = Y_rdd.map(lambda x : len(x)).take(1)[0]
print("Feature count: ", feature_count)

# COMMAND ----------

if load_previous:
    W = load_weights()
else:
    # initialize weights randomly
    W = np.random.normal(0, .1, (feature_count, class_count))
print("Weight matrix shape", W.shape)

# defining gradient and loss for a single datapoint
# To be used inside map function of rdd
mu = 0.1

# COMMAND ----------

def gradient(x, y):
    P = softmax(- x @ W, axis=-1)
    grad = np.expand_dims(x, 1) @ (y - np.expand_dims(P, 0)) +  mu * (W > 0).astype(int) - mu * (W < 0).astype(int) 
    return grad

def loss(x, y):
    return  x @ (W @ y) + np.log(np.sum( np.exp(- x @ W))) + mu * np.linalg.norm(W, ord=1)


# THE GRADIENT DESCENT STEP
lr = .1
decay_rate = .99
def add(a: np.ndarray, b: np.ndarray) -> np.ndarray:
    return np.add(a, b)
assert N == data.count(), "Inconsistency in data shape"
N = data.count()
loss_list = []

# COMMAND ----------

data.map(lambda x : np.argmax(x[1])).take(2)

# COMMAND ----------

lr = .1
for i in range(1000):
    W = W - lr / N * data.map(lambda x : gradient(x[0],x[1])).reduce(add)
#     lr = lr * decay_rate** ((i+1) / 100  )
    tot_loss = data.map(lambda x : loss(x[0], x[1])).reduce(add)
    loss_list.append(tot_loss / N)
    print(f"epoch {i} loss {tot_loss / N}")
    
    
#     if i % 10 == 0:
#         write_weights(W)
#         write_loss(loss_list)

# COMMAND ----------

# MAGIC %md
# MAGIC ### Prediction and Accuracy



predict = lambda x: np.argmax(softmax(- x @ W))
label_data = data.map(lambda x : (np.argmax(x[1]), predict(x[0])))
# Accuracy
print("accuracy ", label_data.map(lambda x : int(x[0] == x[1])).reduce(add) / N)



from sklearn.metrics import confusion_matrix

print(confusion_matrix(label_data.map(lambda x: x[0]).collect(), label_data.map(lambda x: x[1]).collect()))

# COMMAND ----------

test_text_rdd = test_df.select("tweet").rdd
test_tfidf_rdd = test_text_rdd.map(lambda x : get_tfidf(x[0]))
# tfidf_rdd.collect()
test_X_rdd = test_tfidf_rdd
test_class_rdd = test_df.select("class").rdd
test_class_rdd = test_class_rdd.map(lambda x : x[0])

# COMMAND ----------

test_data = test_tfidf_rdd.zip(test_class_rdd)
test_N = test_data.count()
test_N

# COMMAND ----------

test_label_data = test_data.map(lambda x : (x[1], predict(x[0])))
# Accuracy
print("accuracy ", test_label_data
      .map(lambda x : int(x[0] == x[1])).reduce(add) / test_N)

print(confusion_matrix(test_label_data.map(lambda x: x[0]).collect(), test_label_data.map(lambda x: x[1]).collect()))

# COMMAND ----------


