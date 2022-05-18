import argparse
import os
import cv2

def process_img(img_path):
    frame_orig = cv2.imread(img_path)
    ### Replace the code below to show only the check and apply transform.
    frame_result = cv2.cvtColor(frame_orig, cv2.COLOR_BGR2RGB)
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
            