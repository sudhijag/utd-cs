from pyspark.ml import Pipeline
from pyspark.ml.feature import HashingTF, IDF, Tokenizer,StopWordsRemover, StringIndexer
from pyspark.ml.classification import LogisticRegression
from pyspark.ml.tuning import CrossValidator, ParamGridBuilder
from pyspark.ml.evaluation import MulticlassClassificationEvaluator
from pyspark.mllib.evaluation import MulticlassMetrics

#========
#https://transtats.bts.gov/DL_SelectFields.aspx?gnoyr_VQ=FIM&QO_fu146_anzr=Nv4%20Pn44vr45
params = spark.read.text("/FileStore/tables/A2_2_input.txt").collect()
print(params)

#=======
#parameters-later shift to read from file
inputURL= params[0].value
outputURL= params[1].value
 
print("Inputs: " + inputURL + " " + outputURL)

#=========
inputfn= inputURL.split("/")[-1]
print(inputfn)

#==========
from pyspark import SparkFiles
sc.addFile(inputURL)
SparkFiles.get(inputfn)
 
df = spark.read.csv("file://"+ SparkFiles.get(inputfn), header= True, inferSchema= True)
print(df.show(5))

#============
filtered = df.filter("text is not null")
filtered.show()

#==========
#split data into 60 training 40 test
training, test = filtered.randomSplit([0.6, 0.4], seed = 0)
training.cache()
training.show(5)


#=========
tokenizer = Tokenizer(inputCol="text", outputCol="words")
remover = StopWordsRemover(inputCol=tokenizer.getOutputCol(), outputCol="filtered_words")
hashingTF = HashingTF(inputCol=remover.getOutputCol(), outputCol="rawfeatures")
hashingTF.setNumFeatures(15)
idf = IDF(inputCol=hashingTF.getOutputCol(), outputCol="features")
stringIndexer = StringIndexer(inputCol="airline_sentiment", outputCol="label")
lr = LogisticRegression(maxIter=10, regParam=0.001)
 
#==========
#initialize pipeline with phases
pipeline = Pipeline(stages=[tokenizer, remover, hashingTF, idf, stringIndexer, lr])
grid = ParamGridBuilder().addGrid(hashingTF.numFeatures, [10, 100, 1000]).addGrid(lr.regParam, [1.0, 2.0]).build()

#========
evaluator = MulticlassClassificationEvaluator()
cv = CrossValidator(estimator=pipeline, estimatorParamMaps=grid, evaluator=evaluator, numFolds=3, parallelism=2)

cvModel = cv.fit(training)

#=========
testRdd=test.rdd.map(tuple)
test1 = testRdd.first()
predictions = cvModel.transform(test)
filteredPrediction = predictions.select(["airline_sentiment", "prediction"])
filteredPrediction.show(1000)

#=========
dict = {'negative':0.0, 'neutral':1.0, 'positive':2.0}
from pyspark.sql.functions import udf
from pyspark.sql.types import FloatType
 
user_func =  udf (lambda x: dict.get(x), FloatType())
newdf = filteredPrediction.withColumn('airline_sentiment',user_func(filteredPrediction.airline_sentiment))

#=========
mappedPrediction = newdf.rdd.map(tuple)
print(mappedPrediction.collect())
# Instantiate metrics object
#predictionAndLabels = mappedPrediction.map(lambda lp: (mappedPrediction, lp.label))
metrics = MulticlassMetrics(mappedPrediction)

#========
metrics.confusionMatrix().toArray()
metrics.accuracy

for x in [0.0, 1.0, 2.0]:
    print(metrics.precision(x))

for x in [0.0, 1.0, 2.0]:
    print(metrics.recall(x))

for x in [0.0, 1.0, 2.0]:
    print(metrics.fMeasure(x))
