# spark-java-sdk

[![license](https://img.shields.io/github/license/ciscospark/spark-java-sdk.svg)](https://github.com/ciscospark/spark-java-sdk/blob/master/LICENSE)

> The Cisco Spark Java SDK

The Cisco Spark Java SDK is a Java library for consuming Cisco Spark's RESTful APIs. Please visit us at https://developer.ciscospark.com/ for more information about Cisco Spark for Developers.

## Table of Contents

- [Install](#install)
- [Usage](#usage)
- [License](#license)

## Install
This project is compiled with Java 1.6 and [Apache Maven](https://maven.apache.org/).

```bash
git clone git@github.com:ciscospark/spark-java-sdk
cd spark-java-sdk
mvn install
```

The library was developed using the [Java API for JSON Processing](http://www.oracle.com/technetwork/articles/java/json-1973242.html). An implementation of JSONP must be present in the classpath. A reference implementation is available from the [Glassfish project](http://search.maven.org/remotecontent?filepath=org/glassfish/javax.json/1.0.4/javax.json-1.0.4.jar) or via Maven Central:

```xml
<dependency>
  <groupId>org.glassfish</groupId>
  <artifactId>javax.json</artifactId>
  <version>1.0.4</version>
</dependency>
```

## Usage

Below is an example of the SDK in action

```java
import com.ciscospark.*;
import java.net.URI;

class Example {
    public static void main(String[] args) {

        // To obtain a developer access token, visit https://developer.ciscospark.com
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


        // Get person details
        Person person=new Person();
        person=spark.people().path("/<<<**Insert PersonId**>>>").get();

        System.out.println("ID - " + person.getId());
        System.out.println("DisplayName - " + person.getDisplayName());
        System.out.println("Emails - " + Arrays.toString(person.getEmails()));
        System.out.println("FirstName - " + person.getFirstName());
        System.out.println("LastName - " + person.getLastName());
        System.out.println("Avatar - " + person.getAvatar());
        System.out.println("OrgID - " + person.getOrgId());
        System.out.println("Roles - " + Arrays.toString(person.getRoles()));
        System.out.println("Licenses - " + Arrays.toString(person.getLicenses()));
        System.out.println("Created - " + person.getCreated());
        System.out.println("TimeZone - " + person.getTimeZone());
        System.out.println("Status - " + person.getStatus());
        System.out.println("Type - " + person.getType());


        // Update avatar
        person.setAvatar("https://developer.ciscospark.com/images/logo_spark_lg@256.png");
        person=spark.people().path("/<<<**Insert PersonId**>>>").put(person);


        // Create a new webhook
        Webhook webhook=new Webhook();
        webhook.setName("My Webhook");
        webhook.setResource("messages");
        webhook.setEvent("created");
        webhook.setFilter("mentionedPeople=me");
        webhook.setSecret("SOMESECRET");
        webhook.setTargetUrl(URI.create("http://www.example.com/webhook"));
        webhook=spark.webhooks().post(webhook);


        // List webhooks
        spark.webhooks().iterate().forEachRemaining(hook -> {
            System.out.println(hook.getId() + ": " + hook.getName() + " (" + hook.getTargetUrl() + ")" + " Secret - " + hook.getSecret());
        });


        // Delete a webhook
        webhook=spark.webhooks().path("/<<<**Insert WebhookId**>>>").delete();


        // List people in the organization
        spark.people().iterate().forEachRemaining(ppl -> {
        System.out.println(ppl.getId() + ": " + ppl.getDisplayName()+" : Creation: "+ppl.getCreated());
        });


        // Get organizations
        spark.organizations().iterate().forEachRemaining(org -> {
        System.out.println(org.getId() + ": " + org.getDisplayName()+" : Creation: "+org.getCreated());
        });


        // Get licenses
        spark.licenses().iterate().forEachRemaining(license -> {
        System.out.println("GET Licenses " +license.getId() + ": DisplayName:- " + license.getDisplayName()+" : totalUnits:         "+Integer.toString(license.getTotalUnits())+" : consumedUnits: "+Integer.toString(license.getConsumedUnits()));
        });


        // Get roles
        spark.roles().iterate().forEachRemaining(role -> {
        System.out.println("GET Roles " +role.getId() + ": Name:- " + role.getName());
        });


        // Create a new team
        Team team = new Team();
        team.setName("Brand New Team");
        team = spark.teams().post(team);


        // Add a coworker to the team
        TeamMembership teamMembership = new TeamMembership();
        teamMembership.setTeamId(team.getId());
        teamMembership.setPersonEmail("wile_e_coyote@acme.com");
        spark.teamMemberships().post(teamMembership);


        // List the members of the team
        spark.teamMemberships()
                .queryParam("teamId", team.getId())
                .iterate()
                .forEachRemaining(member -> {
                    System.out.println(member.getPersonEmail());
                });
    }
}
```

## License

&copy; 2015-2017 Cisco Systems, Inc. and/or its affiliates. All Rights Reserved.

See [LICENSE](LICENSE) for details.
