# spark-java-sdk
A Java library for consuming RESTful APIs for Cisco Spark.  Please visit us at http://developer.ciscospark.com/.

# Installing
This project is compiled with Java 1.6 and [Apache Maven](https://maven.apache.org/)

    $ git clone git@github.com:ciscospark/spark-java-sdk
    $ cd spark-java-sdk
    $ mvn install

The library was developed using the [Java API for JSON Processing](http://www.oracle.com/technetwork/articles/java/json-1973242.html).
An implementation of JSONP must be present in the classpath.  A reference implementation is available from the 
[Glassfish project](http://search.maven.org/remotecontent?filepath=org/glassfish/javax.json/1.0.4/javax.json-1.0.4.jar) or via 
Maven Central:

    <dependency>
      <groupId>org.glassfish</groupId>
      <artifactId>javax.json</artifactId>
      <version>1.0.4</version>
    </dependency>


Additionally 2 other dependencies must be provided:

        <dependency>
		<groupId>com.fasterxml.jackson.core</groupId>
		<artifactId>jackson-databind</artifactId>
		<version>2.7.3</version>
	</dependency>
        <dependency>
                <groupId>javax.servlet</groupId>
                <artifactId>servlet-api</artifactId>
                <version>2.5</version>
                <scope>provided</scope>
        </dependency>


# Examples

Below is an example of the SDK in action

```java
import com.ciscospark.*;
import java.net.URI;

class Example {
    public static void main(String[] args) {
        // To obtain a developer access token, visit http://developer.ciscospark.com
        String accessToken = "<<secret>>";

        // Initialize the client
        Spark spark = Spark.builder()
                .baseUrl(URI.create("https://api.ciscospark.com/v1"))
                .accessToken(accessToken)
                .build();

        // List the rooms that I'm in
        spark.rooms()
                .iterate()
                .forEachRemaining(room -> {
                    System.out.println(room.getTitle() + ", created " + room.getCreated() + ": " + room.getId());
                });

        // Create a new room
        Room room = new Room();
        room.setTitle("Hello World");
        room = spark.rooms().post(room);


        // Add a coworker to the room
        Membership membership = new Membership();
        membership.setRoomId(room.getId());
        membership.setPersonEmail("wile_e_coyote@acme.com");
        spark.memberships().post(membership);


        // List the members of the room
        spark.memberships()
                .queryParam("roomId", room.getId())
                .iterate()
                .forEachRemaining(member -> {
                    System.out.println(member.getPersonEmail());
                });


        // Post a text message to the room
        Message message = new Message();
        message.setRoomId(room.getId());
        message.setText("Hello World!");
        spark.messages().post(message);


        // Share a file with the room
        message = new Message();
        message.setRoomId(room.getId());
        message.setFiles(URI.create("http://example.com/hello_world.jpg"));
        spark.messages().post(message);
    }
}
```
To Receive events from your webhook, Servlets are used.  Any container can be used, for this example we will use jetty.

Jetty requires the following extra dependencies:

        <dependency>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-servlet</artifactId>
                <version>8.1.16.v20140903</version>
        </dependency>
        <dependency>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-server</artifactId>
                <version>8.1.16.v20140903</version>
        </dependency>

```java
import com.ciscospark.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

class WebhookExample {
    public static void main(String[] args) {

                Server server = new Server(8080);
                ServletHandler context = new ServletHandler();
                server.setHandler(context);

                SparkServlet sparkServlet = new SparkServlet();
                context.addServletWithMapping(new ServletHolder(sparkServlet), "/*");

		sparkServlet.addListener(new WehookEventListener() {
				public void onEvent(WebhookEvent event) {
					System.out.println("Got a message with ID: " + event.getData().getId());
				}
			});

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}


```

# Legalese

Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
