# Setup

Note: all these instructions assume you are in the root project directory
unless otherwise stated.

* Run

  `mvn install`
  mvn install --file server-pom.xml

* If you want to create an Eclipse project, also run

  `mvn eclipse:eclipse`

  Then, in Eclipse, File -> Import -> Existing Project and select this
  directory. Then, Window -> Preferences -> Classpath Variables -> New.
  Name it M2_REPO, and give it the path to ~/.m2/repository.

# Non-web version

You can use maven to build the indexing/searching executable, or you can run it
in Eclipse. If running in Eclipse, make sure to provide a command line argument.

* To create the jar with Maven, run

  `mvn --file command-line-pom.xml package`

* This creates a directory called target/, and the jar is inside. To run the
  jar, do

  `java -jar target/GOOSE-CL-0.1-SNAPSHOT.jar --index`

  to index the files in the crawl/courses/ directory

* To run the search engine, run

  `java -jar target/GOOSE-CL-0.1-SNAPSHOT.jar --search`

# Evaluation

Once you get the judgements.txt file and additional crawled files, you will be
able to run the evaluation script. To compile it, use Eclipse or run maven with
the following command:

`mvn --file eval-pom.xml package`

Then you can run

`java -jar target/GOOSE-EVAL-0.1.SNAPSHOT.jar`

to see your MAP score.

# Web version

You are *not required* to use the web version. In fact, it will probably not
work on the EWS machines, so you would have to use your own system and install
the necessary packages.

## Setup

* Install required gems:

  `cd web/`

  `bundle install`

* Set up nodejs:

  `cd web/node/`

  `npm install`

## Running the server locally

* Start the rails server:

  `cd web/`

  `rails s`

* Start the jetty server:

  `mvn --file server-pom.xml package`

  `java -jar target/GOOSE-WEB-0.1-SNAPSHOT.jar`

* Start the proxy:

  `cd web/node`

  `node proxy.js`

* (Make sure that you have indexed the files!)

* Now you can navigate to http://localhost:9001/ and use the search engine!
