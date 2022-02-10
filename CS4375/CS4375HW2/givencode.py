#####################################################################################################################
#   Assignment 2: Neural Network Programming
#   This is a starter code in Python 3.6 for a 1-hidden-layer neural network.
#   You need to have numpy and pandas installed before running this code.
#   Below are the meaning of symbols:
#   NeuralNet class init method takes file path as parameter and splits it into train and test part
#         - it assumes that the last column will the label (output) column
#   h - number of neurons in the hidden layer
#   X - vector of features for each instance
#   y - output for each instance
#   W_hidden - weight matrix connecting input to hidden layer
#   Wb_hidden - bias matrix for the hidden layer
#   W_output - weight matrix connecting hidden layer to output layer
#   Wb_output - bias matrix connecting hidden layer to output layer
#   deltaOut - delta for output unit (see slides for definition)
#   deltaHidden - delta for hidden unit (see slides for definition)
#   other symbols have self-explanatory meaning
#   You need to complete all TODO marked sections
#   You are free to modify this code in any way you want, but need to mention it in the README file.
#
#####################################################################################################################


import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler

class NeuralNet:
    def __init__(self, dataFile, header=True, h=4):
        #np.random.seed(1)
        # train refers to the training dataset
        # test refers to the testing dataset
        # h represents the number of neurons in the hidden layer
        raw_input = pd.read_csv(dataFile, sep= '\t', header= None)
        
        print("RAW INPUT: \n", raw_input.head())
        
        # TODO: Remember to implement the preprocess method
        processed_data = self.preprocess(raw_input)
        
        self.train_dataset, self.test_dataset = train_test_split(processed_data)
        ncols = len(self.train_dataset.columns)
        nrows = len(self.train_dataset.index)
        
        ncols2 = len(self.test_dataset.columns)
        nrows2 = len(self.test_dataset.index)
        
        
        print("Testing Data: \n", self.test_dataset.head())
        print(self.test_dataset.shape)
        
        
        self.X = self.train_dataset.iloc[:, 0:(ncols -1)].values.reshape(nrows, ncols-1)
        self.y = self.train_dataset.iloc[:, (ncols-1)].values.reshape(nrows, 1)
        
        #TODO: added
        self.X_test = self.test_dataset.iloc[:, 0:(ncols2 -1)].values.reshape(nrows2, ncols2-1)
        self.y_test = self.test_dataset.iloc[:, (ncols2-1)].values.reshape(nrows2, 1)
        
        #
        # Find number of input and output layers from the dataset
        #
        input_layer_size = len(self.X[1])
        if not isinstance(self.y[0], np.ndarray):
            self.output_layer_size = 1
        else:
            self.output_layer_size = len(self.y[0])

        # assign random weights to matrices in network
        # number of weights connecting layers = (no. of nodes in previous layer) x (no. of nodes in following layer)
        self.W_hidden = 2 * np.random.random((input_layer_size, h)) - 1
        self.Wb_hidden = 2 * np.random.random((1, h)) - 1

        self.W_output = 2 * np.random.random((h, self.output_layer_size)) - 1
        self.Wb_output = np.ones((1, self.output_layer_size))

        self.deltaOut = np.zeros((self.output_layer_size, 1))
        self.deltaHidden = np.zeros((h, 1))
        self.h = h

    #
    # TODO: I have coded the sigmoid activation function, you need to do the same for tanh and ReLu
    #

    def __activation(self, x, activation):
        if activation == "sigmoid":
            self.__sigmoid(self, x)
        if activation == "tanh":
            self.__tanh(self, x)  
        if activation == "relu":
            self.__relu(self, x)       
    

    #
    # TODO: Define the derivative function for tanh, ReLu and their derivatives
    #

    def __activation_derivative(self, x, activation):
        if activation == "sigmoid":
            self.__sigmoid_derivative(self, x)
        if activation == "tanh":
            self.__tanh_derivative(self,x)
        if activation == "relu":
            self.__relu_derivative(self,x)
    
    def __tanh(self, x):
        #return (  (np.exp(x) - np.exp(-x)) / (np.exp(x) + np.exp(-x))  )
        #return (np.exp(x) - np.exp(-x)) / (np.exp(x) + np.exp(-x))
        return np.tanh(x)
    
    def __tanh_derivative(self, x):
        #return 1 - (  (np.exp(x) - np.exp(-x)) / (np.exp(x) + np.exp(-x))  )**2
        return 1- x*x

    def __relu(self, x):
        return np.maximum(0,x)
        
    def __relu_derivative(self,x):
        x[x>0] = 1
        x[x<= 0] = 0
        return x
    
    def __sigmoid(self, x):
        return 1 / (1 + np.exp(-x))

    # derivative of sigmoid function, indicates confidence about existing weight

    def __sigmoid_derivative(self, x):
        return x * (1 - x)

    #
    # TODO: Write code for pre-processing the dataset, which would include standardization, normalization,
    #   categorical to numerical, etc
    #

    def preprocess(self, X):
        X= X.dropna()
        X= X.drop(X.columns[8], axis=1)#car name
        X= X.drop(X.columns[7], axis=1)#origin
        X= X.drop(X.columns[6], axis=1)#model year
        
        #X = preprocessing.normalize([X])
        
        scaler = MinMaxScaler()
        X[X.columns] = scaler.fit_transform(X[X.columns])

        return X

    # Below is the training function

    def train(self, max_iterations=60000, learning_rate=0.125):
        for iteration in range(max_iterations):
            out = self.forward_pass(activation="relu")
            error = 0.5 * np.power((out - self.y), 2)
            # TODO: I have coded the sigmoid activation, you have to do the rest
            self.backward_pass(out, activation="relu")

            update_weight_output = learning_rate * np.dot(self.X_hidden.T, self.deltaOut)
            update_weight_output_b = learning_rate * np.dot(np.ones((np.size(self.X, 0), 1)).T, self.deltaOut)

            update_weight_hidden = learning_rate * np.dot(self.X.T, self.deltaHidden)
            update_weight_hidden_b = learning_rate * np.dot(np.ones((np.size(self.X, 0), 1)).T, self.deltaHidden)

            self.W_output += update_weight_output
            self.Wb_output += update_weight_output_b
            self.W_hidden += update_weight_hidden
            self.Wb_hidden += update_weight_hidden_b

        print("After " + str(max_iterations) + " iterations, the total error is " + str(np.sum(error)))
        print("The final weight vectors are (starting from input to output layers) \n" + str(self.W_hidden))
        print("The final weight vectors are (starting from input to output layers) \n" + str(self.W_output))

        print("The final bias vectors are (starting from input to output layers) \n" + str(self.Wb_hidden))
        print("The final bias vectors are (starting from input to output layers) \n" + str(self.Wb_output))

    def forward_pass(self, activation):
        # pass our inputs through our neural network
        in_hidden = np.dot(self.X, self.W_hidden) + self.Wb_hidden
        # TODO: I have coded the sigmoid activation, you have to do the rest
        if activation == "sigmoid":
            self.X_hidden = self.__sigmoid(in_hidden)
        if activation == "tanh":
            self.X_hidden = self.__tanh(in_hidden) 
        if activation == "relu":
            self.X_hidden = self.__relu(in_hidden)
            
        in_output = np.dot(self.X_hidden, self.W_output) + self.Wb_output
        
        if activation == "sigmoid":
            out = self.__sigmoid(in_output)
        if activation == "tanh":
            out = self.__tanh(in_output)   
        if activation == "relu":
            out = self.__relu(in_output)  
            
        return out

    def backward_pass(self, out, activation):
        # pass our inputs through our neural network
        self.compute_output_delta(out, activation)
        self.compute_hidden_delta(activation)

    # TODO: Implement other activation functions    
    def compute_output_delta(self, out, activation):
        if activation == "sigmoid":
            delta_output = (self.y - out) * (self.__sigmoid_derivative(out))
        if activation == "tanh":
             delta_output = (self.y - out) * (self.__tanh_derivative(out))
        if activation == "relu":
             delta_output = (self.y - out) * (self.__relu_derivative(out))

        self.deltaOut = delta_output

    def compute_hidden_delta(self, activation):
        if activation == "sigmoid":
            delta_hidden_layer = (self.deltaOut.dot(self.W_output.T)) * (self.__sigmoid_derivative(self.X_hidden))
        if activation == "tanh":
            delta_hidden_layer = (self.deltaOut.dot(self.W_output.T)) * (self.__tanh_derivative(self.X_hidden))
        if activation == "relu":
            delta_hidden_layer = (self.deltaOut.dot(self.W_output.T)) * (self.__relu_derivative(self.X_hidden))
        self.deltaHidden = delta_hidden_layer

    # TODO: Implement the predict function for applying the trained model on the  test dataset.
    # You can assume that the test dataset has the same format as the training dataset
    # You have to output the test error from this function

    def predict(self, header = True,activation="relu"):
        # TODO: obtain prediction on self.test_dataset
        in_hidden = np.dot(self.X_test, self.W_hidden) + self.Wb_hidden
        # TODO: I have coded the sigmoid activation, you have to do the rest
        if activation == "sigmoid":
            self.X_hidden = self.__sigmoid(in_hidden)
        if activation == "tanh":
            self.X_hidden = self.__tanh(in_hidden) 
        if activation == "relu":
            self.X_hidden = self.__relu(in_hidden)
            
        in_output = np.dot(self.X_hidden, self.W_output) + self.Wb_output
        
        if activation == "sigmoid":
            out = self.__sigmoid(in_output)
        if activation == "tanh":
            out = self.__tanh(in_output)   
        if activation == "relu":
            out = self.__relu(in_output)  
          
        errors = 0.5 * np.power((out - self.y_test), 2)
        return sum(errors)


if __name__ == "__main__":
    url= "https://raw.githubusercontent.com/sudhijag/test/master/auto-mpg%20-%20auto-mpg.tsv"
    
    neural_network = NeuralNet(url)
    neural_network.train()
    testError = neural_network.predict()
    print("Test error = " + str(testError))
