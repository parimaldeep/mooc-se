from videoToText import videoToTextConverter


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

