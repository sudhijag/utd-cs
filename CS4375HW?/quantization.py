from skimage import io
from sklearn.cluster import KMeans
import numpy as np
import matplotlib.pyplot as plt

#I used images 1, 4, and 5. You can just change the digit in the url below.
image= io.imread("https://personal.utdallas.edu/~axn112530/cs6375/unsupervised/images/image5.jpg")

dimensions=image.shape
width= dimensions[1]
height= dimensions[0]

image = np.array(image) / 255
image = np.reshape(image, (height*width, 3))

model = KMeans(n_clusters=4, random_state=666)
model.fit(image)
#labels=model.labels_
#print(labels[0])

image = np.empty((height, width, 3))
for i in range(height):
    for j in range(width):
        image[i][j] = model.cluster_centers_[model.labels_[width*i + j]]
plt.imshow(image)

#io.imsave('my_image.png',image)