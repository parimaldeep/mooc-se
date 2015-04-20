from subprocess import call
# from pytesser import *
# 
# mainDir = '.'
# files = os.listdir(mainDir)
# fname = os.path.join(mainDir, 'phototest.tif')
# im = Image.open(fname)
# text = image_to_string(im)
# print text

call(['./tesseract','phototest.tif', 'out1.txt', '-l', 'eng']);
