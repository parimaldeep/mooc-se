import cv2 as cv2
import numpy as np
import math

def videoToFrameConverter(fileName):
    
    cap = cv2.VideoCapture('data/' + fileName)
    count  = 0
    prevFrame = None
    msec = 0
    times = []
    while(cap.isOpened()):
        cap.set(0, msec)
        msec = msec + 10000
        ret, frame = cap.read()
        
        if ret == False:
            break
        
        if prevFrame is not None and frame is not None:
            res = cv2.matchTemplate(frame, prevFrame, cv2.TM_CCORR_NORMED)
            if res >= 0.99:
                continue;
            
        cv2.imwrite('process/frame' +str(int(math.floor(msec/1000)))+ '.jpg', frame)     # save frame as JPEG file
        
        times.append(int(math.floor(msec/1000)))
        if cv2.waitKey(10) == 27:                     # exit if Escape is hit
            break
        count += 1
        prevFrame = frame; 
    
    cap.release()
    cv2.destroyAllWindows()
    
    return times