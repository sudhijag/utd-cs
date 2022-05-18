#homework3-2

import random
import tweepy
import time
import kafka
from kafka import KafkaProducer, KafkaConsumer
from pyspark.ml import Pipeline
from pyspark.ml.classification import LogisticRegression
from pyspark.ml.tuning import CrossValidator, ParamGridBuilder
from pyspark.ml.evaluation import MulticlassClassificationEvaluator
from pyspark.mllib.evaluation import MulticlassMetrics
import sys
from pyspark.sql import SparkSession
from pyspark.sql.functions import explode
from pyspark.sql.functions import split
import logging
from tweepy import Stream
from tweepy import OAuthHandler
from tweepy.streaming import Stream
import json


producer = KafkaProducer(bootstrap_servers="localhost:9092")

class listener(Stream):
    def on_data(self, data):
        try:
            msg = json.loads(data)
            print("SENDING:", msg['text'].encode('utf-8'))
            producer.send("test", msg['text'].encode('utf-8') )
            return True

        except BaseException as e:
            print("ERROR", e)
            return True

    def on_error(self, status):
        print("ERROR WITH STATUS", status)
        return False

def main():
    if len(sys.argv) != 2:
        print("Usage: tweetfetcher.py <query>", file=sys.stderr)
        sys.exit(-1)

    query= sys.argv[1]

    spark = SparkSession.builder.appName("Homework3_tweet").getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")

    logging.basicConfig(filename="twKafkaApp.log", level=logging.DEBUG)
    logger = logging.getLogger(__name__)
    logging.info('Started')

    #consumer key, consumer secret, access token, access secret.
    consumerKey= "6wiX7IqnZKHetMISzcYIsTl3l"
    consumerSecret= "FhzZ8oGOYtCs0gl1LqbJcldCDwOodaDzlVeG4XTa9AjjhwZ4z2"
    accessToken= "2182214210-PmRGeJVKPQnDz7ft7thPHdGVH94sqiur5YEjPKj"
    accessSecret= "TrqU8tZlIhTK0bMCZI3WgnVWsbnkLZoH02jVdyRS0jzpk"

    twitterStream = listener(consumer_key= consumerKey, consumer_secret= consumerSecret, access_token= accessToken,access_token_secret= accessSecret)

    #listofcontroversialtopics= []
    twitterStream.filter(track=[query], languages=["en"]) #TODO: add query rules, remove non-english tweets



    logging.info('Finished')

if __name__ == "__main__":
    main()