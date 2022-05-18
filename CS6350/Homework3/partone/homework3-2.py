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

from pyspark.sql import SparkSession
from pyspark.sql.functions import explode
from pyspark.sql.functions import split
from pyspark.sql.functions import udf

from pyspark.sql.types import LongType
from pyspark.sql.types import StringType
from pyspark.sql.functions import col
from textblob import TextBlob
import sys


def main():
	if len(sys.argv) != 5:
	    print("Usage: homework3-2.py <bootstrap-servers> <checkpoint-dir> <input-topic> <output-topic>", file=sys.stderr)
	    sys.exit(-1)

	bootstrapServers = sys.argv[1]
	checkpointDir = sys.argv[2]
	inputtopic= sys.argv[3]
	outputtopic= sys.argv[4]
	subscribeType="subscribe"

	spark = SparkSession.builder.appName("Homework3").getOrCreate()
	spark.sparkContext.setLogLevel("ERROR")


	lines = spark.readStream.format("kafka").option("kafka.bootstrap.servers", bootstrapServers)\
	    .option(subscribeType, inputtopic)\
	    .option("failOnDataLoss", "false")\
	    .load()\
	    .selectExpr("CAST(value AS STRING)")

	def dummy_function(tweet_text):
		sentiment=TextBlob(tweet_text).sentiment.polarity

		#roughly splitting (-1,1) into thirds
		if(sentiment < -0.33):
			return "negative"
		elif(sentiment > 0.33):
			return "positive"
		else:
			return "neutral"

	dummy_udf= udf(dummy_function, StringType())

	lines = lines.withColumn("sentiment", dummy_udf(lines['value']))
	lines = lines.withColumnRenamed("value","msg") \
    .withColumnRenamed("sentiment","value")


	#query = lines.writeStream.outputMode('append').format('console').start().awaitTermination()
	#query = lines.writeStream.format("kafka").outputMode('append').\
	#option("checkpointLocation", checkpointDir).\
	option("kafka.bootstrap.servers", bootstrapServers).option("topic", outputtopic).start().awaitTermination()

if __name__ == "__main__":
    main()