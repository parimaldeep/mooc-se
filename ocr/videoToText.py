import os

from pytesser import *
from videoToFrame import videoToFrameConverter


def videoToTextConverter(videoLink, filename, num):
    os.system('rm -f process/frame* process/output*');
    times = videoToFrameConverter(filename);

    for time in times:
        os.system('tesseract -l eng process/frame' + str(time) + '.jpg process/output'+ str(time));
        with open('videos/videoText' + str(num)+ '-' + str(time) + '.txt', 'w') as outfile:

            videoID = videoLink.split("v=")[1];
            embedURL = "http://www.youtube.com/embed/" + videoID + "?start=" + str(time);
            
            outfile.write(embedURL + '\n');
            outfile.write(filename.split('.mp4')[0] + '\n')
            fname = 'process/output' + str(time) + '.txt'
            with open(fname) as infile:
                for line in infile:
                    outfile.write(line)
    
        with open('videos/videos.txt', 'a') as videoResultfile:
            videoResultfile.write('videoText' + str(num)+ '-' + str(time) + '.txt\n')