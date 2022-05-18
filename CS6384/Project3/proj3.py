import argparse
import os
import cv2
import cv2
import numpy as np 
import argparse
import sys
from os import listdir
from os.path import isfile, join
import math

DEBUGMODE=0

def findclosest(contour, targetpoint):
    bestdistance= 9999999
    currdistance= 0
    bestpoint= [-10,-10]

    for mypoint in contour:
        currdistance= (np.linalg.norm(targetpoint-mypoint))

        if(currdistance < bestdistance):
            bestpoint= mypoint[0]
            bestdistance= currdistance    
    
    return bestpoint

   
def detect_check(frame) :
    #STEP 1: CONVERT GRAYSCALE AND RESIZE
    dims=frame.shape
    newdims=[1290, 790]

    frame= cv2.resize(frame,(1290,790)) 
    step1=cv2.cvtColor(frame,cv2.COLOR_BGR2GRAY) 

    #STEP 2: GAUSSIAN BLUR
    step1=cv2.GaussianBlur(step1,(5,5),0)  

    #STEP 3: ADAPTIVE THRESHOLD
    step1 = cv2.adaptiveThreshold(step1, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY_INV, 15, 11)

    if(DEBUGMODE):
        cv2.imshow("THRESH", step1)
        cv2.waitKey(0)
        cv2.destroyAllWindows()

    #STEP 4: FIND CONTOURS
    allcontours, unused =cv2.findContours(step1,cv2.RETR_LIST,cv2.CHAIN_APPROX_NONE)
    allcontours=sorted(allcontours,key=cv2.contourArea)
    maxc= allcontours[len(allcontours)-1]

    #STEP 5: APPROXIMATE MAX CONTOUR
    approx_maxc= cv2.approxPolyDP(maxc, 0.01* cv2.arcLength(maxc,True),True) 

    if(DEBUGMODE):
        cv2.drawContours(frame, approx_maxc, -1, (0, 255, 0), 3)
        cv2.imshow("APPXMAX CONTOUR", frame)
        cv2.waitKey(0)
        cv2.destroyAllWindows()

    #STEP 6: FIND CORNERS W/ MATH
    pt1= findclosest(approx_maxc, [0, 0])
    pt2= findclosest(approx_maxc, [newdims[0], 0])
    pt3= findclosest(approx_maxc, [0, newdims[1]])
    pt4= findclosest(approx_maxc, [newdims[0], newdims[1]])
    print("TRANSFORMING WITH: \n", pt1, pt2, pt3, pt4)

    if(DEBUGMODE):
        cv2.circle(frame, pt1, 10, (255,0,0), 5)
        cv2.circle(frame, pt2, 10, (0,255,0), 5)
        cv2.circle(frame, pt3, 10, (0,0,255), 5)
        cv2.circle(frame, pt4, 10, (255,255,255), 5)
        cv2.imshow("CORNERS", frame)
        cv2.waitKey(0)
        cv2.destroyAllWindows()

    #if for some reason the points found are garbage, we want to salvage using boxpoints.
    distances=  [ np.linalg.norm(pt1-pt2), np.linalg.norm(pt1-pt3), np.linalg.norm(pt1-pt4), np.linalg.norm(pt2-pt3), np.linalg.norm(pt2-pt4), np.linalg.norm(pt2-pt3) ]

    distthresh=50
    for el in distances:
        if el < distthresh:
            print("Error in finding corners. Attempting boxpoints...")
            rect = cv2.minAreaRect(maxc)
            src = np.float32(cv2.boxPoints(rect))
            dst= [[0,0],[400,0],[0,800],[400,800]]
            dst= np.float32(dst)  
            ptrans =cv2.getPerspectiveTransform(src,dst)  
            final=cv2.warpPerspective(frame, ptrans,(800,400))

            return final

    #STEPS 7 AND 8: ROTATE IF NEEDED AND PERFORM PERSPECTIVE TRANSFORM
    src= np.float32([pt1, pt2, pt3, pt4])
    if(dims[0] > dims[1]): 
        print("ROTATING....")
        dst= [[0,0],[400,0],[0,800],[400,800]]
        dst= np.float32(dst)  
        ptrans =cv2.getPerspectiveTransform(src,dst)  
        final=cv2.warpPerspective(frame, ptrans,(400,800))
        final = cv2.rotate(final, cv2.cv2.ROTATE_90_CLOCKWISE)
    else:
        dst= [[0,0],[800,0],[0,400],[800,400]]
        dst= np.float32(dst)  
        ptrans =cv2.getPerspectiveTransform(src,dst)  
        final=cv2.warpPerspective(frame, ptrans,(800,400))

    return final

def process_img(img_path):
    frame_orig = cv2.imread(img_path)
    ### Replace the code below to show only the check and apply transform.
    #frame_result = cv2.cvtColor(frame_orig, cv2.COLOR_BGR2RGB)
    print("RUNNING DETECTION ON", img_path)
    frame_result= detect_check(frame_orig)
    print("==============\n\n")
    ### Replace the code above.
    cv2.imshow("Original", frame_orig)
    cv2.imshow("Result", frame_result)
    cv2.waitKey(0)
    
if __name__=="__main__":
    parser = argparse.ArgumentParser(description="Check prepartion project")
    parser.add_argument('--input_folder', type=str, default='samples', help='check images folder')
    
    args = parser.parse_args()
    input_folder = args.input_folder
   
    for check_img in os.listdir(input_folder):
        img_path = os.path.join(input_folder, check_img)
        if img_path.lower().endswith(('.png','.jpg','.jpeg', '.bmp', '.gif', '.tiff')):
            process_img(img_path)
            