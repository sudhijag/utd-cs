import cv2
import numpy as np 
import argparse
import sys
from os import listdir
from os.path import isfile, join

def detect_lane(frame) :
    result = 0
    return result

###########################################################################

def runon_image(path) :
    frame = cv2.imread(path)
    # Change the codes here
    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    detections_in_frame = detect_lane(frame)
    frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
    # Change the codes above
    cv2.imshow("one image", frame)
    cv2.waitKey(0)
    return detections_in_frame

def runon_folder(path) :
    files = None
    if(path[-1] != "/"):
        path = path + "/"
        files = [join(path,f) for f in listdir(path) if isfile(join(path,f))]
    all_detections = 0
    for f in files:
        print(f)
        f_detections = runon_image(f)
        all_detections += f_detections
    return all_detections

if __name__ == '__main__':
    # command line arguments
    parser = argparse.ArgumentParser()
    parser.add_argument('-folder', help="requires path")
    args = parser.parse_args()
    folder = args.folder
    if folder is None :
        print("Folder path must be given \n Example: python proj1.py -folder images")
        sys.exit()

    if folder is not None :
        all_detections = runon_folder(folder)
        print("total of ", all_detections, " detections")

    cv2.destroyAllWindows()



