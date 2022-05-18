#read parameters
params = spark.read.text("dbfs:/FileStore/shared_uploads/sxj180060@utdallas.edu/hw2_1_input-2").collect()
 
inputURL= params[0].value
maxiter= params[1].value
outputURL= params[2].value
 
print("Inputs: " + inputURL + " " + maxiter +  " " + outputURL)

#========

inputfn= inputURL.split("/")[-1]
print(inputfn)
 
from pyspark import SparkFiles
 
sc.addFile(inputURL)
fileobj= SparkFiles.get(inputfn)
 
print(fileobj)


#========

rdd = sc.textFile("file://"+ fileobj)
header = rdd.first()
rdd = rdd.filter(lambda x: x != header)
print(rdd.take(5))
 
rdd2= rdd.flatMap(lambda x: x.split("\n"))
rdd3= rdd2.map(lambda x: x.split(","))
 
print(rdd3.take(5))

#========

outlinks= rdd3.map(lambda x: (x[0],1) ).reduceByKey(lambda x,y : x+y)
 
print(outlinks.take(5))

#========

rdd4= rdd3.map(lambda x: (x[0], x[3]))
 
print(rdd4.take(5))
#========

N= rdd4.distinct().count()
 
print(N)
#========
#we flip because we are interested in the t for each x
flipped= rdd4.map(lambda x: (x[1], x[0]))
print(flipped.take(5))
#========
#the outlink is the outlink of t (x[0])
test= flipped.join(outlinks)
print(test.take(1))

#========

#initializing the pageranks to 10
pageranks=rdd4.keys().distinct()
pageranks= pageranks.map(lambda x: (x,10))
 
print(pageranks.take(5))

#========
#This code is the same as the one in the loop
test2= test.join(pageranks)
test3= test2.map(lambda x: (x[0], (x[1][0][0],x[1][0][1], x[1][1], x[1][0][1]/x[1][1])  )  )
test4= test3.map(lambda x: (x[0], x[1][3])).reduceByKey(lambda x,y: x+y)
pageranks= test4.map(lambda x: (x[0], (0.15 * 1/N )+ 0.85*x[1])).sortBy(lambda x: -x[1])
 
print("PAGERANKS FOR ITER 1: ", pageranks.take(10))
    
for itera in range(1, (int) (maxiter) ):
    #print("===STARTING PR====", pageranks.take(5), end ="\n")
    test2= test.join(pageranks).sortBy(lambda x: -x[1][1])
    
    #start, end, ol, pr, pr/ol
    test3= test2.map(lambda x: (x[0], (x[1][0][0],x[1][0][1], x[1][1], x[1][1]/x[1][0][1])  )  ).sortBy(lambda x: -x[1][1])
    
    #remove extra columns, we only need start, and pr/ol
    test4= test3.map(lambda x: (x[0], x[1][3])).reduceByKey(lambda x,y: x+y)
    
    #modify pageranks
    pageranks= test4.map(lambda x: (x[0], (0.15 * 1/N )+ 0.85*x[1])).sortBy(lambda x: -x[1])
    
    print("==== PAGERANKS FOR ITER ", itera + 1, " :", pageranks.take(10))
    print("\n")

#========
pageranks_export= pageranks.map(lambda x: str(x))
pageranks_aslist= pageranks_export.collect()
pageranks_exportstr = ' '.join(pageranks_aslist)
 
print(pageranks_exportstr)


