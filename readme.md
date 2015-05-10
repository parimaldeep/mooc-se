1. We can go to https://github.com/parimaldeep/mooc-se and download zip or clone the project on our server or desktop.
2. We need to install OpenCV. Homebrew can be used to install it on Mac.
  $ brew install cmake
  $ brew install ffmpeg
  $ brew install opencv --python27 --ffmpeg
3. Then we need to install libpng too. MacPorts can be used to install it:
  $ port install xorg-server
  $ port install xorg-libXmu
  $ port install libxml2
  $ port install libpng
4. Next we need to setup Tesseract. Homebrew can be used to install it on Mac.
  $ brew install tesseract
5. Videos that we want to index should be placed in {root}/ocr/data/. {root}/ocr/data/videoNames.txt should contain the video URL in one line and video title in the next line.
6. Next we need to start the script to convert video to text form. This is done by running:
  $ python {root}/ocr/videosToText.py
7. Then we need to create search engine using Lucene.
   $ cd {root}
   $ module load apache-maven
   $ mvn install --file server-pom.xml
   $ mvn --file command-line-pom.xml package
   $ java -jar target/GOOSE-CL-0.1-SNAPSHOT.jar --index
8. We need to install required gems:
   $ cd {root}/web/
   $ bundle install
9. We need to set up nodejs:
   $ cd {root}/web/node/
   $ npm install
10. Start the rails server:
   $ cd {root}/web/
   $ rails s
11. Start the jetty server:
   $ mvn --file server-pom.xml package
   $ java -jar target/GOOSE-WEB-0.1-SNAPSHOT.jar
12. Start the proxy:
   $ cd web/node
   $ node proxy.js
13. We can navigate to http://localhost:9001/ and use the search en- gine.
14. Create a file at {root}/judgements.txt to write test data. The format is: First line: query text. Second line: space delimited list of relevant URLs. Repeat the same with other queries.
15. Once we get the judgements.txt file , we can run the evaluation script. To compile it and execute, run:
   $ mvn --file eval-pom.xml package
   $ java -jar target/GOOSE-EVAL-0.1-SNAPSHOT.jar

Basic search engine taken from CS410(Spring 2014) assignment 