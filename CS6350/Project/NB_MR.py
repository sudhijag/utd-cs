# Databricks notebook source
inputURL= "https://raw.githubusercontent.com/sudhijag/bdproject/main/labeled_dat.csv"

# COMMAND ----------

inputfn= inputURL.split("/")[-1]
print(inputfn)

from pyspark import SparkFiles

sc.addFile(inputURL)
fileobj= SparkFiles.get(inputfn)

print(fileobj)

# COMMAND ----------

#rdd = spark.read.option("delimiter", ",").csv("file://"+ fileobj)
df = spark.read.option("header","true").option("inferSchema","true").csv("file://"+ fileobj)
df = df.where(df.tweet.isNotNull() )
print(df.count())

rdd= df.rdd.map(tuple)
print(rdd.take(5))

"""rdd= rdd.map(lambda x: x.split(','))
header = rdd.first()
rdd = rdd.filter(lambda x: x != header)
print(rdd.take(5))
print(rdd.count())"""

# COMMAND ----------

rdd2= rdd.map(lambda x: (x[5], x[6] ))
print(rdd2.take(5))

# COMMAND ----------

#convert to df
df= rdd2.toDF()

print(df.show())

# COMMAND ----------

from pyspark.ml.feature import Tokenizer, StopWordsRemover

tokenizer = Tokenizer(inputCol="_2", outputCol="_3")
df2= tokenizer.transform(df)

print(df2.show(5))

swremover = StopWordsRemover(inputCol="_3", outputCol="_4")
df3= swremover.transform(df2)
print(df3.show(5))

# COMMAND ----------

from pyspark.sql import Row
rdd3 = df3.rdd.map(tuple)
rdd4= rdd3.map(lambda x: (x[0], x[3]))
print(rdd4.first())


# COMMAND ----------

import re
def removePunctuation(text):
    #<FILL IN>
    ans1 = text.lower()
    #re.sub(pattern, repl, string, count=0, flags=0)
    #ans2 = re.sub('[^A-Za-z\s\d]', "", ans1)
    #print ans2
    ans2 = ans1.strip()
    for element in ans2:
        #print(ans2.find('@'))
        if(element.find("@") == 0): #regular words, not hashtags
            ans3 = ans2.replace(element, ' ')
        if(element.find("#") == 0):
            # print(el)
            ans3 = re.sub('\W+',' ', element)
            ans3 = ans3.replace(element[0], ' ')
    ans3 = re.sub('\W+',' ', element)
    return ans3
rdd5= rdd4.map(lambda x: str(x[0]) + " "+ ' '.join(x[1]))


# COMMAND ----------

removePunctuation('We now have an RDD that is only words. Next, lets @apply the wordCount() function to produce a list of word counts.')

# COMMAND ----------

#run the rest of naive bayes
rdd6 = rdd2.flatMap(lambda x: (x[0], x[1].split(" ")))
rdd6.take(2)

# COMMAND ----------

from collections import Counter

import numpy as np
import string
import requests
import nltk
nltk.download('punkt')
nltk.download('stopwords')
from nltk.corpus import stopwords
from scipy.special import softmax


stop_words=set(stopwords.words('english'))

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

	
	return cleantokens3

# COMMAND ----------

!pip install nltk

# COMMAND ----------

def frequency(tweet):
    frequency = {}
    for word in preprocessing_tweets(tweet):
        frequency[word] = frequency.get(word,0) + 1
    return frequency
    

# COMMAND ----------

rdd2.take(5)

# COMMAND ----------

rdd6 = rdd2.flatMap(lambda x:[((word, x[0]), count) for word,count in frequency(x[1]).items()] )
rdd6.take(5)

# COMMAND ----------

rdd7 = rdd6.reduceByKey(lambda x,y: x+y) 
sample_dict = rdd7.collectAsMap()
print(sample_dict)
print(type(sample_dict))


# COMMAND ----------

totals = rdd7.map(lambda x: (x[0][1], x[1]))
totals.take(10)
termTotals = totals.reduceByKey(lambda x,y: x+y)
termTotals.take(3)

# COMMAND ----------

totalT = rdd2.map(lambda x: (x[0], 1))
totalTweets = totalT.reduceByKey(lambda x,y: x+y)
totalTweets.take(3)
y= totalTweets.collect()
print(type(y[2][1]))
print(y[2][1])

# COMMAND ----------

words = rdd6.map(lambda x: x[0][0]).distinct()
unique_words = words.collect()
print(type(unique_words))

# COMMAND ----------

nb_training(rdd2)

# COMMAND ----------

def nb_training(tweetList):
    frequency1={}
    #get every intance of a term with regard to a class
    rdd6 = tweetList.flatMap(lambda x:[((word, x[0]), count) for word,count in frequency(x[1]).items()] )
    
    #combine terms to get frequency of a term across all tweets from sam class
    rdd7 = rdd6.reduceByKey(lambda x,y: x+y) 
    frequency1 = rdd7.collectAsMap()
    
    # get the total number of terms that make up all the tweets in a class
    totals = rdd7.map(lambda x: (x[0][1], x[1]))
    totals.take(10)
    termTotals = totals.reduceByKey(lambda x,y: x+y)
    terms = termTotals.collect()
    
    # Get the total number of tweets per class
    totalT = tweetList.map(lambda x: (x[0], 1))
    totalTweets = totalT.reduceByKey(lambda x,y: x+y)
    tt = totalTweets.collect()
    
    #Get the unique words used in all the tweets
    words = rdd6.map(lambda x: x[0][0]).distinct()
    unique_words = words.collect()
    
    tTotals = tt[0][1] + tt[1][1]+ tt[2][1]
    
    #calaculate the prior probabilities
    hatePrior = tt[2][1] / tTotals
    regularPrior = tt[0][1] / tTotals
    offensivePrior = tt[1][1] / tTotals
    
    regWordProbability = {}
    offensiveProbability = {}
    hateWordProbablity = {}
    alpha = .0001
    
    #loop through all the distinct words and assign in a probability against a certain class if it is present in a tweet that falls under that class
    for x in unique_words:

       #Check wether the term is in the class if not set an alpha value so that it doesn result in a multiply by zero later
       #probabilities for neither tweets
        regFrequency = frequency1.get((x,'2'), 0)
        #print(regFrequency)
        if regFrequency == 0 :
            regWordProbability[x] = alpha / terms[0][1]
        else:
            #print('Enter the reg')
            regWordProbability[x] = regFrequency / terms[0][1]
        #probabilities for offensive tweets
        offensiveFrequency = frequency1.get((x, '1'), 0)
        if offensiveFrequency == 0 :
            offensiveProbability[x] = alpha / terms[1][1]
        else:
            #print('Enter the off')
            offensiveProbability[x] = offensiveFrequency / terms[1][1]
        #probabilities for hateful tweets
        hateFrequency = frequency1.get((x, '0'), 0)
        if hateFrequency == 0 :    
            hateWordProbablity[x] = alpha / terms[2][1]
        else:
            #print('Enter the hate')
            hateWordProbablity[x] = hateFrequency / terms[2][1]
    
    return regularPrior, offensivePrior, hatePrior, regWordProbability , offensiveProbability, hateWordProbablity
        

# COMMAND ----------

regularPrior, offensivePrior, hatePrior, regWordProbability , offensiveProbability, hateWordProbablity = nb_training(rdd2)

nb_predict('@CanIFuckOrNah: What would yall lil ugly bald headed bitches do if they stop making make-up &amp', regularPrior, offensivePrior, hatePrior, regWordProbability , offensiveProbability, hateWordProbablity):

# COMMAND ----------

def nb_predict(tweet, regularPrior, offensivePrior, hatePrior, regWordProbability , offensiveProbability, hateWordProbablity):
    
    
    
    #print(regWordProbability)
    
    tweetList = process_tweet(tweet)
    #print(tweetList)
    hateProb = 1
    regProb = 1
    offensiveProb = 1
    #each word in thw tweets multiplies its probability value to show the tweets classification probability
    for x in tweetList:
        #print('hi')
        word = str(x)
        if x in regWordProbability:
            #print(regWordProbability[word])
            regProb = regProb * regWordProbability[word]
        if x in hateWordProbability:
            #print(hateWordProbability[word])
            hateProb = hateProb * hateWordProbability[word]
        if x in offensiveWordProbablity:
            #print(offensiveWordProbablity[word])
            offensiveProb = offensiveProb * offensiveWordProbablity[word]
            
    regProb = regProb * regularPrior
    hateProb = hateProb * hatePrior
    offensiveProb = offensiveProb * offensivePrior
    #print(regProb, hateProb, offensiveProb)     
    #Classifies by the class with the max probability
    if regProb == max(regProb, hateProb, offensiveProb):
        return regProb, 2
    elif offensiveProb == max(regProb, hateProb, offensiveProb) :
        return offensiveProb, 1
    else:
        return hateProb, 0
