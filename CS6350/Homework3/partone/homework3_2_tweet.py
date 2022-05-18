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


def main():
	if len(sys.argv) != 4:
	    print("Usage: homework3_2_tweet.py <bootstrap-servers> <query> <topics>", file=sys.stderr)
	    sys.exit(-1)

	bootstrapServers = sys.argv[1]
	query = sys.argv[2]
	topic= sys.argv[3]


	spark = SparkSession.builder.appName("Homework3_tweet").getOrCreate()
	spark.sparkContext.setLogLevel("ERROR")

	client= tweepy.Client(bearer_token= "AAAAAAAAAAAAAAAAAAAAAKzWbAEAAAAAOtgmMfBPBi4JhmtWPI%2F70u8%2FJ8k%3D2pzqP1Hn7HLH0CUr5pBAxoCCMhgjyQ75M6154iXYVc1eV5w3Ei")
	producer = KafkaProducer(bootstrap_servers=bootstrapServers)


	while True:
		response= client.search_recent_tweets(query=query, max_results=20)
		print(response)

		for tweet in response.data:
			#perform sentiment analysis
			print("TEXT", tweet.text)
			producer.send(topic, (tweet.text).encode("UTF-8") )

		time.sleep(60)

if __name__ == "__main__":
    main()