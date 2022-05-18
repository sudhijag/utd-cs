import cv2
import numpy as np 
import argparse
import sys
from os import listdir
from os.path import isfile, join
import math

PI= 3.141592

def roi(frame, x_range, y_range):
    #x_range 0 to 400
    #y_range 0 to 113

    for x in range(x_range[0], x_range[1]):
        for y in range(y_range[0], y_range[1]):
           frame[y,x]= 0 #black it out!
    return frame

def remove_bad_lines(rgbframe, lines):
    newlines= []
    if lines is not None:
        for line in lines:
            x1, y1, x2, y2 = line[0]

            #print(x1, y1, x2, y2)

            if x1 != x2:
                angle = math.atan( (y1 - y2 )/ (x1- x2) ) * 180;
                angle /= PI;
            else:
                angle= 999;
           
            #take out flat lines
            anglethresh=15
            if(( angle >= 1*anglethresh and angle <= 1* (180-anglethresh) )  or ( angle >= -1*(180-anglethresh) and angle <= -1* anglethresh ) ):
                if( ( ispointwhite ([x1,y1], rgbframe) or ispointyellow([x1,y1], rgbframe) ) or  ( ispointwhite ([x2,y2], rgbframe) or ispointyellow([x2,y2], rgbframe) ) ):
                    #print("COLOR", rgbframe[y1, x1])
                    #print("ANGLE" ,angle)
                    newlines.append( [[x1, y1, x2, y2]] ) 
                    #cv2.line(frame, (x1, y1), (x2, y2), (0, 255, 0), 3)  

        return newlines
    else:
        return None

def remove_duplicates(lines):
    if(len(lines) == 1):
        return lines

    finalidx=[]
    #perform one last check to remove duplicate lines
    for i in range(len(lines)):
        for j in range(i+1, len(lines)):
            if(i == j):
                continue;
            else:
                #find distance
                 x1, y1, x2, y2 = lines[i][0]
                 ax1, ay1, ax2, ay2 = lines[j][0]

                 #euclidean distance
                 new1= [ax1- x1, ay1-y1]
                 new2= [ax2- x2, ay2-y2]

                 #distance=  ( ( (ax1- x1) ** 2 ) +  ( (ay1- y1) ** 2 ) ) ** 0.5 +  ( ( (ax2- x2) ** 2 ) + ( (ay2- y2) ** 2 ) ) ** 0.5
                 distance= np.linalg.norm(new1) + np.linalg.norm(new2)
                 distancethresh=80

                 print("DISTANCE BETWEEN ", i, j, distance)

                 if(distance < distancethresh):
                    print("REMOVING LINE", i)
                    finalidx.append(i)
    print("FINALIDX IS: ", finalidx)

    returnlines=[]
    for i in range(len(lines)):
        if i not in finalidx:
            returnlines.append(lines[i])

    return returnlines

def ispointwhite(point, frame): 
    #change to examine two pixel neighborhood
    pixel_value= frame[point[1], point[0]]

    red= pixel_value[0]
    green= pixel_value[1]
    blue= pixel_value[2]

    if(red >0 and red < 255 and green > 200 and green < 255 and blue > 0 and blue < 255):
        return True
    else:
        return False

def ispointyellow(point, frame): 
    pixel_value= frame[point[1], point[0]]
    red= pixel_value[0]
    green= pixel_value[1]
    blue= pixel_value[2]

    if(red >10 and red < 40 and green > 0 and green < 255 and blue > 100  and blue < 255):
        return True
    else:
        return False


def detect_lane(frame) :
    hsvframe = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    rgbframe = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

    #step 1:convert grayscale (resize is performed in runon_image)
    step1 = cv2.cvtColor(rgbframe, cv2.COLOR_RGB2GRAY)

    #step 2: equalize and blur
    step2 = cv2.equalizeHist(step1)
    step2 = cv2.medianBlur(step2, 3)

    #step 3: threshold
    step3 = cv2.threshold(step2, 170, 255, cv2.THRESH_BINARY_INV)[1]

    #step 4: erode and dilate
    step4 = cv2.morphologyEx(step3, cv2.MORPH_OPEN, (3,3))
    step4 = cv2.morphologyEx(step4, cv2.MORPH_CLOSE, (3,3))

    #step 5: canny
    step5 = cv2.Canny(step4, 50, 200)

    #step 6: region of interest
    x_range=[0,400]
    y_range=[0,113]
    step6 = roi(step5, x_range, y_range)

    #step 7: hough transform
    step7 = cv2.HoughLinesP(step6, 1, PI/180, threshold= 12, minLineLength= 50, maxLineGap=100) 
    if step7 is None:
        return 0
    print("STEP 7", len(step7))
    
    #step 8: remove bad lines
    step8= remove_bad_lines(rgbframe, step7)
    if step8 is None:
        return 0
    print("STEP 8", len(step8))

    #step 9: remove duplicates
    step9= remove_duplicates(step8)
    if step9 is None:
        return 0
    print("FINALLINES", len(step9))


    if step9 is None:
        return 0

    for i in range(len(step9)):
            x1, y1, x2, y2 = step9[i][0]

            cv2.line(frame, (x1, y1), (x2, y2), (255, 0, 0), 3) 

    return len(step9)

###########################################################################

def runon_image(path) :
    frame = cv2.imread(path)

    # Change the codes here
    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    frame = cv2.resize(frame, (400,200))
    detections_in_frame = detect_lane(frame)
    frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
    
    # Change the codes above

    cv2.imshow("one image", frame)
    cv2.waitKey(0)

    return detections_in_frame

def runon_folder(path) :
    print("PATH" , path)

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

    #cv2.destroyAllWindows()