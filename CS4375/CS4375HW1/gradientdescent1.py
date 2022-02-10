import pandas as pd
from sklearn.linear_model import SGDRegressor
import numpy as np
import sympy as sym
import matplotlib.pyplot as plt

"""
    0. mpg: continuous
1. cylinders: multi-valued discrete
2. displacement: continuous
3. horsepower: continuous
4. weight: continuous
    5. acceleration: continuous
6. model year: multi-valued discrete
//7. origin: multi-valued discrete
//8. car name: string (unique for each instance)
"""

#1. Choose dataset
url="https://raw.githubusercontent.com/sudhijag/test/master/auto-mpg%20-%20auto-mpg.tsv"

#df= pd.read_excel("auto-mpg.xlsx", header=None)
df= pd.read_csv(url, sep= '\t')

#2. Pre-process
#df= df.drop(df.columns[8], axis=1)#car name
#df= df.drop(df.columns[7], axis=1)#origin
df= df.dropna()

#3. Train-test split
x= df.iloc[:,5] #acceleration
y=df.iloc[:,0] #mpg

boundary=int(0.8*len(y))

train_y= y[0:boundary]
test_y= y[boundary:]

train_x= x[0:boundary]
test_x = x[boundary:]

#4. Do linear regression
b0,b1=0.0,0.0
rate=0.001

for i in range(100):
    print(i)
    
    y_predicted_vector = train_x
    y_predicted_vector *= b1
    y_predicted_vector += b0
    residual_vector= train_y-y_predicted_vector
    
    b0_adjustment= (np.sum(residual_vector)/len(residual_vector)) *-2
    
    b1_temp= np.dot(train_x,residual_vector)
    b1_adjustment= (np.sum(b1_temp)/len(train_x)) * (-2)
    
    b1 -= (rate * b1_adjustment)
    b0 -= (rate * b0_adjustment)
    print(b0, b1)

#5. test
RMSE=0

y_final_predictions = test_x
y_final_predictions *= b1
y_final_predictions += b0

residual_test_vector= (y_final_predictions - test_y )** 2
RMSE= sum(residual_test_vector)
RMSE= RMSE/len(residual_test_vector)
RMSE= RMSE ** 0.5
print(RMSE)  

