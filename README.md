# spark-java-sdk
A Java library for consuming RESTful APIs for Cisco Spark.  Please visit us at http://developer.ciscospark.com/.

# Installing
This project is compiled with Java 1.8 and [Apache Maven](https://maven.apache.org/)

  $ git clone git@github.com:ciscospark/spark-java-sdk
  $ cd spark-java-sdk
  $ mvn install

The library was developed using the [Java API for JSON Processing](http://www.oracle.com/technetwork/articles/java/json-1973242.html), 
as such an implementation must be present in the classpath.  A reference implementation for JSONP is available from the 
[Glassfish project](http://search.maven.org/remotecontent?filepath=org/glassfish/javax.json/1.0.4/javax.json-1.0.4.jar) or via 
Maven Central:

    <dependency>
      <groupId>org.glassfish</groupId>
      <artifactId>javax.json</artifactId>
      <version>1.0.4</version>
    </dependency>
