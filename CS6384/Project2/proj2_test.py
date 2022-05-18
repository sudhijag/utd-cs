import pandas as pd
import argparse
import tensorflow as tf
import os
import cv2

# Note that you can save models in different formats. Some format needs to save/load model and weight separately.
# Some saves the whole thing together. So, for your set up you might need to save and load differently.

def load_model_weights(model, weights = None):
    my_model = tf.keras.models.load_model(model)
    my_model.summary()
    return my_model

def get_images_labels(df, classes, img_height, img_width):
    test_images = []
    test_labels = []
    # Write the code as needed for your code
    for index, row in df.iterrows():
        print("ROW", row)

        label = row["label"]
        img = tf.io.read_file(row["image_path"])
        img = decode_img(img, img_height, img_width)

        test_images.append(img)
        
        #ADDED
        flowers_to_num = ["astilbe", "bellflower", "black-eyed susan", "calendula", "california poppy","carnation", "common daisy", "coreopsis", "dandelion", "iris", "rose", "sunflower", "tulip"]
        if label not in flowers_to_num:
            print("ERROR: YOUR CSV CONTAINS A CLASS:", label, "NOT IN THE 13 CLASSES: ", flowers_to_num)
            
        test_labels.append(flowers_to_num.index(label))

    #ADDED
    test_images= tf.convert_to_tensor(test_images)
    test_labels= tf.convert_to_tensor(test_labels)

    return test_images, test_labels

def decode_img(img, img_height, img_width):
    # Convert the compressed string to a 3D uint8 tensor
    img = tf.io.decode_jpeg(img, channels=3)
    # Resize the image to the desired size
    return tf.image.resize(img, [img_height, img_width])

if __name__=="__main__":
    parser = argparse.ArgumentParser(description="Transfer Learning Test")
    parser.add_argument('--model', type=str, default='my_model.h5', help='Saved model')
    parser.add_argument('--weights', type=str, default=None, help='weight file if needed')
    parser.add_argument('--test_csv', type=str, default='flowers_test.csv', help='CSV file with true labels')

    args = parser.parse_args()
    model = args.model
    weights = args.weights
    test_csv = args.test_csv

    test_df = pd.read_csv(test_csv, sep= ", ")
    classes = {'astilbe', 'bellflower', 'black-eyed susan', 'calendula', 'california poppy','carnation', 'common daisy', 'coreopsis', 'dandelion', 'iris', 'rose', 'sunflower', 'tulip'}

    print(test_df);

    # Rewrite the code to match with your setup
    test_images, test_labels = get_images_labels(test_df, classes, 256, 256)
    
    my_model = load_model_weights(model)
    loss, acc = my_model.evaluate(test_images, test_labels, verbose=2)
    print('Test model, accuracy: {:5.5f}%'.format(100 * acc))

    