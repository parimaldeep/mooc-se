f = open('urls.txt','w')
f1 = open('courses.txt','w')

i = 0

with open('edx-urls.txt') as fp:
    for line in fp:
        i = i + 1
        f.write(str(i) + " " +line)
        f1.write("courses/" + str(i) + ".txt\n")
with open('khan-urls.txt') as fp:
    for line in fp:
        i = i + 1
        f.write(str(i) + " " +line)
        f1.write("courses/" + str(i) + ".txt\n")
with open('udacity-urls.txt') as fp:
    for line in fp:
        i = i + 1
        f.write(str(i) + " " +line)
        f1.write("courses/" + str(i) + ".txt\n")
with open('udemy-urls.txt') as fp:
    for line in fp:
        i = i + 1
        f.write(str(i) + " " +line)
        f1.write("courses/" + str(i) + ".txt\n")
#with open('coursera-urls.txt') as fp:
#    for line in fp:
#        i = i + 1
#        f.write(str(i) + " " +line)
#        f1.write("courses/" + str(i) + ".txt\n")
