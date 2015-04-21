from videoToText import videoToTextConverter
import time

start_time = time.time()

num = 1
videoNames = 'data/videoNames.txt'

open('videos/videos.txt', 'w')

with open(videoNames) as videoFile:
    for line in videoFile:
        videoLink = line
        videoLink = videoLink.strip('\n')
        videoName = next(videoFile)
        videoName = videoName.strip('\n')
        print "Starting video number " + str(num) + ' ' + videoName + '...'
        videoToTextConverter(videoLink, videoName + '.mp4', num)
        num = num + 1

print("--- %s seconds ---" % (time.time() - start_time))