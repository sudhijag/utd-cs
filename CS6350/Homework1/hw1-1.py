%sh
/databricks/python3/bin/pip install spacy 
/databricks/python3/bin/python3 -m spacy download en_core_web_sm

#======
import sys
import spacy

#======
# download any large book from gutenberg.org
input = sc.textFile("dbfs:/FileStore/shared_uploads/sxj180060@utdallas.edu/frankenstein-1.txt") # input
rawtext=' '.join(input.collect())
finder = spacy.load("en_core_web_sm")
newtext= finder(rawtext)

#=======
print(type(newtext.ents))
NElist=[entity.text for entity in newtext.ents]
NE= sc.parallelize(NElist)
NE.collect()

#========
NEPairs = NE.map(lambda x: (x, 1))
NECounts = NEPairs.reduceByKey(lambda x,y: x + y).sortBy(lambda x: -x[1])
NECounts.collect()

#=======
