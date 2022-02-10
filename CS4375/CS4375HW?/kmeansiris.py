import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from sklearn.cluster import KMeans

url= "https://raw.githubusercontent.com/sudhijag/test/master/iris.tsv"
df= pd.read_csv(url, sep=',', header=None)

df=df.dropna()

X= df.iloc[:,0:3]
y=df.iloc[:, 4]

#print(X)
#print(y)

#for i in range(1,21):
model = KMeans(n_clusters= 3, random_state=666)

model.fit(X)

centers = model.cluster_centers_
print(centers)

SSE= model.inertia_
#print("SSE for ", i , ": ")
print(SSE)

