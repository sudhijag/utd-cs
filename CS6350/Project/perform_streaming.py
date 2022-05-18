from collections import Counter
import numpy as np
from scipy.special import softmax
import nltk
from nltk.corpus import stopwords
from pyspark.sql import SparkSession
from nltk.corpus import stopwords
import random
import tweepy
import time
import json
import kafka
from kafka import KafkaProducer, KafkaConsumer
from pyspark.ml import Pipeline
from pyspark.ml.classification import LogisticRegression
from pyspark.ml.tuning import CrossValidator, ParamGridBuilder
from pyspark.ml.evaluation import MulticlassClassificationEvaluator
from pyspark.mllib.evaluation import MulticlassMetrics
from pyspark.sql import SparkSession
from pyspark.sql.functions import explode
from pyspark.sql.functions import split
from pyspark.sql.functions import udf
from pyspark.sql.types import LongType
from pyspark.sql.types import StringType, FloatType
from pyspark.sql.functions import col
from textblob import TextBlob
import sys
from tweepy import Stream
from tweepy import OAuthHandler
from tweepy.streaming import Stream
import string
from pyspark.sql.functions import to_json, col, struct
from collections import Counter
import numpy as np
import nltk
import requests

url_path = "https://raw.githubusercontent.com/mandalbiswadip/data_storage/main/log_regression_weights/"
l1_url_path = "https://raw.githubusercontent.com/mandalbiswadip/data_storage/main/log_regression_weights_l1/"
l2_url_path = "https://raw.githubusercontent.com/mandalbiswadip/data_storage/main/log_regression_weights_l2/"


nb_url_path= "https://raw.githubusercontent.com/sudhijag/bdproject/main/"

#nbhate_url_path= "https://raw.githubusercontent.com/sudhijag/bdproject/main/hateWordProbablity.txt"
#nboff_url_path= "https://raw.githubusercontent.com/sudhijag/bdproject/main/offensiveProbability.txt"
#nbreg_url_path= "https://raw.githubusercontent.com/sudhijag/bdproject/main/regWordProbability.txt"
#nbprior_url_path= "https://raw.githubusercontent.com/sudhijag/bdproject/main/Prior.txt"

stop_words=set(stopwords.words('english'))
import sys


unique_words = None
W = None
idf_dict = None
word_index_dict = None

l1_unique_words = None
l1_W = None
l1_idf_dict = None
l1_word_index_dict = None

l2_unique_words = None
l2_W = None
l2_idf_dict = None
l2_word_index_dict = None

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
		if el not in STOP_WORDS and el not in stop_words:
			cleantokens3.append(el)

# 	print("TWEET after removing", cleantokens3)
	#first 1 gram then 2 gram

	
	return " ".join(cleantokens3)


def read_file(filename, url):
	print("IN READ_FILE", filename , url)
	response = requests.get(f"{url}/{filename}")
	data = response.text
	return data

def load_idf_dict(url):
    return {line.split()[0]: float(line.split()[1]) for line in read_file("idf_dict.txt", url).splitlines()}

def load_word_list(url):
    return read_file("vocab.txt", url).splitlines()

def load_weights(url):
    weight_text = read_file("weights.txt", url)
    return np.array(
        [[float(y) for y in x.split("\t")] for x in weight_text.splitlines()]
    )


def load_model(url):
	u_w_temp = load_word_list(url)
	W_temp = load_weights(url)
	idf_dict_temp = load_idf_dict(url)


	word_index_dict_temp = dict()
	for index, word in enumerate(u_w_temp):
		word_index_dict_temp[word] = index
	return u_w_temp, W_temp, idf_dict_temp, word_index_dict_temp

def get_tfidf(sentence, unique_words_p, idf_dict_p, word_index_dict_p):
    words = nltk.word_tokenize(sentence)
    vec = np.zeros(len(unique_words_p) + 1)
    vec[-1] = 1 # this is for the bias term

    for word, v in Counter(words).items():
        if word in word_index_dict_p:
            vec[word_index_dict_p[word]] = v * idf_dict_p[word]
    return vec

predict = lambda x, weight: np.argmax(softmax(- x @ weight))

def get_prediction(tweet):
	
	feature = get_tfidf(preprocessing_tweets(tweet), unique_words, idf_dict, word_index_dict)
	if(float(predict(feature, W)) == 0):
		return "Hate"
	if(float(predict(feature, W)) == 1):
		return "Offensive"
	if(float(predict(feature, W)) == 2):
		return "Neutral"

def get_prediction_l1(tweet):

	feature = get_tfidf(preprocessing_tweets(tweet), l1_unique_words, l1_idf_dict, l1_word_index_dict)
	if(float(predict(feature, l1_W)) == 0):
		return "Hate"
	if(float(predict(feature, l1_W)) == 1):
		return "Offensive"
	if(float(predict(feature, l1_W)) == 2):
		return "Neutral"

def get_prediction_l2(tweet):
	feature = get_tfidf(preprocessing_tweets(tweet), l2_unique_words, l2_idf_dict, l2_word_index_dict)

	if(float(predict(feature, l2_W)) == 0):
		return "Hate"
	if(float(predict(feature, l2_W)) == 1):
		return "Offensive"
	if(float(predict(feature, l2_W)) == 2):
		return "Neutral"


def nb_predict(tweet):   
	"""regularPrior, offensivePrior, hatePrior = readPriors("Priors.txt", nb_url_path)
	regWordProbability = readFromFile("regWordProbability.txt", nb_url_path)
	offensiveWordProbablity = readFromFile("offensiveProbability.txt", nb_url_path)
	hateWordProbability = readFromFile("hateWordProbablity.txt", nb_url_path)"""
	tweetList = tweet.split(" ")
	hateProb = 1
	regProb = 1
	offensiveProb = 1
    
	for x in tweetList:
		word=str(x)
		if x in regWordProbability:
			regProb = regProb * regWordProbability[word]
		if x in hateWordProbability:
			hateProb = hateProb * hateWordProbability[word]
		if x in offensiveWordProbablity:
			offensiveProb = offensiveProb * offensiveWordProbablity[word]
         
	#print("PRIORS:", regularPrior,hatePrior,offensivePrior)   
	regProb = regProb * regularPrior
	hateProb = hateProb * hatePrior
	offensiveProb = offensiveProb * offensivePrior
	#print("PROBS:", regProb,hateProb,offensiveProb)  

	if regProb == max(regProb, hateProb, offensiveProb):
		return "Neutral"
	elif offensiveProb == max(regProb, hateProb, offensiveProb) :
		return "Offensive"
	else:
		return "Hate"

def readPriors(filename, url):
    data= read_file(filename, url)
    data= data.split("\n")
    print("DATA IN READ FILE", data)
    reg = (float) (data[0])
    off = (float) (data[1])
    hate = (float) (data[2])
      
    return reg, off, hate
    

def readFromFile(filename, url):
    data= read_file(filename, url)
    
    js = json.loads(data)
  
    return js
    

if __name__ == "__main__":
	if len(sys.argv) != 5:
	    print("Usage: predict_lr <bootstrap-servers> <checkpoint-dir> <input-topic> <output-topic>", file=sys.stderr)
	    sys.exit(-1)

	bootstrapServers = sys.argv[1]
	checkpointDir = sys.argv[2]
	inputtopic= sys.argv[3]
	outputtopic= sys.argv[4]
	subscribeType="subscribe"

	global regularPrior, offensivePrior, hatePrior, regWordProbability, offensiveWordProbablity, hateWordProbability
	regularPrior, offensivePrior, hatePrior = readPriors("Prior.txt", nb_url_path)
	regWordProbability = readFromFile("regWordProbability.txt", nb_url_path)
	offensiveWordProbablity = readFromFile("offensiveProbability.txt", nb_url_path)
	hateWordProbability = readFromFile("hateWordProbablity.txt", nb_url_path)


	if unique_words is None:
		unique_words, W, idf_dict, word_index_dict = load_model(url_path)


	if l1_unique_words is None:
		l1_unique_words, l1_W, l1_idf_dict, l1_word_index_dict = load_model(l1_url_path)


	if l2_unique_words is None:
		l2_unique_words, l2_W, l2_idf_dict, l2_word_index_dict = load_model(l2_url_path)

	spark = SparkSession.builder.appName("Homework3").getOrCreate()
	spark.sparkContext.setLogLevel("ERROR")

	lines = spark.readStream.format("kafka").option("kafka.bootstrap.servers", bootstrapServers)\
	    .option(subscribeType, inputtopic)\
	    .option("failOnDataLoss", "false")\
	    .load()\
	    .selectExpr("CAST(value AS STRING)")

	lr_udf= udf(get_prediction, StringType())
	l1lr_udf= udf(get_prediction_l1, StringType())
	l2lr_udf= udf(get_prediction_l2, StringType())
	nb_udf= udf(nb_predict, StringType())
	clean_udf= udf(preprocessing_tweets, StringType())

	lines = lines.withColumn("CLEANED", clean_udf(lines['value']))
	lines = lines.withColumn("LR", lr_udf(lines['value']))
	lines = lines.withColumn("L1LR", l1lr_udf(lines['value']))
	lines = lines.withColumn("L2LR", l2lr_udf(lines['value']))
	lines = lines.withColumn("NB", nb_udf(lines['CLEANED']))
	lines = lines.withColumn("json", to_json(struct([lines[x] for x in lines.columns])))
	lines = lines.withColumnRenamed("value","msg").withColumnRenamed("json","value")

	#query = lines.writeStream.format("console").outputMode('append').start().awaitTermination()
	query = lines.\
	writeStream.format("kafka").outputMode('append').\
	option("checkpointLocation", checkpointDir).\
	option("kafka.bootstrap.servers", bootstrapServers).option("topic", outputtopic).start().awaitTermination()