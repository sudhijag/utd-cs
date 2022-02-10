import pandas as pd
from sklearn import linear_model
from sklearn import metrics
import numpy as np

#1. Choose dataset
url="https://raw.githubusercontent.com/sudhijag/test/master/auto-mpg%20-%20auto-mpg.tsv"

#df= pd.read_excel("auto-mpg.xlsx", header=None)
df= pd.read_csv(url, sep= '\t')

#2. Pre-process
df= df.drop(df.columns[8], axis=1)#car name
df= df.drop(df.columns[7], axis=1)#origin

df= df.dropna()

#3. Train-test split
X= df.iloc[:,5]
X= df.drop(df.columns[0], axis=1)
y=df.iloc[:,0]
#print(X)


train_X= X[0:312]
test_X= X[312:]

train_y= y[0:312]
test_y= y[312:]

#4. Do linear regression
lm = linear_model.LinearRegression()
model = lm.fit(train_X,train_y)

print("b0:" , lm.intercept_)
print("b1:" , lm.coef_)

#5. Test
print(lm.score(test_X,test_y))
y_predicted= lm.predict(test_X)
RMSE =metrics.mean_squared_error(test_y, y_predicted) ** 0.5
print(RMSE)

