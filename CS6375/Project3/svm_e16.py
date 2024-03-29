# -*- coding: utf-8 -*-
"""SVM-e16

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1SlJCrtZj-7k2voKpcLIygP3espyGFJck
"""

from sklearn import svm, preprocessing
import numpy as np

#====================================
# STEP 1: read the training and testing data.
# Do not change any code of this step.

# specify path to training data and testing data
train_x_location = "x_train16.csv"
train_y_location = "y_train16.csv"

test_x_location = "x_test.csv"
test_y_location = "y_test.csv"

print("Reading training data")
x_train = np.loadtxt(train_x_location, delimiter=",")
y_train = np.loadtxt(train_y_location, delimiter=",")

m, n = x_train.shape # m training examples, each with n features
m_labels,  = y_train.shape # m2 examples, each with k labels
l_min = y_train.min()

assert m_labels == m, "x_train and y_train should have same length."
assert l_min == 0, "each label should be in the range 0 - k-1."
k = y_train.max()+1

print(m, "examples,", n, "features,", k, "categiries.")

print("Reading testing data")
x_test = np.loadtxt(test_x_location, delimiter=",")
y_test = np.loadtxt(test_y_location, delimiter=",")

m_test, n_test = x_test.shape
m_test_labels,  = y_test.shape
l_min = y_train.min()

assert m_test_labels == m_test, "x_test and y_test should have same length."
assert n_test == n, "train and x_test should have same number of features."

print(m_test, "test examples.")

#  ====================================
# STEP 2: pre processing
# Please modify the code in this step.
print("Pre processing data")
# you can skip this step, use your own pre processing ideas,
# or use anything from sklearn.preprocessing

# The same pre processing must be applied to both training and testing data
x_train = preprocessing.scale(x_train)
x_test = preprocessing.scale(x_test)

# ====================================
# STEP 3: train model.
# Please modify the code in this step.

print("---train")
model = svm.SVC(C=10, gamma= 0.005,  kernel= "rbf") # this line should be changed
model.fit(x_train, y_train)

# ====================================
# STEP3: evaluate model
# Don't modify the code below.

print("---evaluate")
print(" number of support vectors: ", model.n_support_)
acc = model.score(x_test, y_test)
print("acc:", acc*100)