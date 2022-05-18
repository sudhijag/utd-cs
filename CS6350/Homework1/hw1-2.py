inputs = sc.textFile("dbfs:/FileStore/shared_uploads/sxj180060@utdallas.edu/plot_summaries.txt") # input
N= inputs.count()

#======

wordcount= inputs.flatMap(lambda x: x.split(" ")).map(lambda x:(x,1)).reduceByKey(lambda x,y:x+y)
print(wordcount.take(5))

#======
import math
idf_values= wordcount.map(lambda x: (x[0], math.log(N/x[1] , math.e))) #32806 films
print(idf_values.take(5))

#=======
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
) #FROM SPACY DOCUMENTATION

#========

#commented out spacy line because it was slowing down my code heavily  ( > 30 minutes )
#cleaninputs= inputs.map(lambda x: ' '.join([i.lemma_ for i in nlp(x) if (not i.is_punct and not i.is_stop)])    )  
cleaninputs= inputs.map(lambda x: ' '.join([i.lower() for i in x.split(" ") if i.lower() not in STOP_WORDS])    )
stripspace= cleaninputs.map(lambda x:' '.join(x.split())   )
 
print(stripspace.take(5))

#=======
#create tuples
startingrdd= stripspace.map(lambda x: (x.split(" ")[0] , ' '.join(x.split(" ")[1:] )) )
startingrdd.take(11)

#========
#we will need this for the cosine sim
queriesandlens= startingrdd.map(lambda x:(x[0], len(x[1])   ))
print(queriesandlens.take(5))

#=======
splittowords=startingrdd.flatMap(lambda x: [(i, x[0]) for i in x[1].split()])
 
print(splittowords.take(20))

#========
temp= splittowords.map(lambda x: (x,1)).map(lambda x:( (x[0][0],  x[0][1]) , x[1] )   )
print(temp.take(5))
tf_values= temp.reduceByKey(lambda x,y:x+y)
print(tf_values.take(2))

#========
#working with df now bc we need to aggregate
from pyspark.sql.functions import col
 
tf_values= tf_values.map(lambda x: (x[0][0], x[0][1], x[1])) #to prevent it from creating structs
 
tf_asdf= tf_values.toDF(["WORD", "ID", "TF"])
idf_asdf= idf_values.toDF(["WORD", "IDF"])
 
print(tf_asdf.show(5))
print(idf_asdf.show(5))

#==========
final_df= tf_asdf.join(idf_asdf,["WORD"], "left")
print(final_df.count())

final_df= final_df.withColumn("TFIDF", final_df.TF*final_df.IDF)
print(final_df.count())

#==========
#load movie metadata to join with the present DF
metadata = spark.read.format("csv").option("delimiter", "\t").option("header", "false").load("dbfs:/FileStore/shared_uploads/sxj180060@utdallas.edu/movie_metadata.tsv")
metadata=metadata.drop("_c1")
metadata=metadata.drop("_c3")
metadata=metadata.drop("_c4")
metadata=metadata.drop("_c5")
metadata=metadata.drop("_c6")
metadata=metadata.drop("_c7")
metadata=metadata.drop("_c8")
metadata = metadata.withColumnRenamed("_c0", "ID").withColumnRenamed("_c2", "NAME")
metadata.show()

#===========
countDF= queriesandlens.toDF(["ID", "QUERY LENGTH"])
 
info_df= metadata.join(countDF, ["ID"])
print(info_df.show())

#===========
"""
tfidf_forquery= final_df.filter( final_df.WORD== "katniss").orderBy("TFIDF")
tfidf_forquery.show()
"""

#===========
#read file
queriesrdd=sc.textFile("dbfs:/FileStore/shared_uploads/sxj180060@utdallas.edu/searchterms-1.txt")
queries= queriesrdd.collect()
 
for query in queries:
    if(len(query.split(" ")) == 1):
        tfidf_forquery= final_df.filter( final_df.WORD== query).orderBy("TFIDF").take(10)
        print("BEST RESULTS FOR QUERY: "+ query+ " ORDERED BY TFIDF")
        for row in tfidf_forquery:
            #TODO: get name from info df
            metaforquery= info_df.filter(info_df.ID == row.ID).take(1)
 
            print((str) (metaforquery[0].NAME) + " " + str(row.TFIDF) )
    else:
        #formula for COSINE SIM: matches/ (sqrt(len(document)) * sqrt(len(query)))
        #but since len(query) is constant, look for matches/ (sqrt(len(document))
        
        """
        holdsresults= []
        
        for term in query:
            tempresult= final_df.filter( final_df.WORD== term).orderBy("TF").take(10)
            tfidf_forquery= holdsresults.append()
            #find freq of that term
        hr= holdsresults[0]
        for hr in holdsresults[1:]:
            hr.join(["ID", "WORD"])
        
        #then do some sort, divide
        """
        
        print("COSINE SIMILARITY FOR QUERY TERM: WIP")
