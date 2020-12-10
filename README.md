<h1 align="center">
    <a href="developer.webex.com"><img src="https://www.webex.com/content/dam/wbx/us/images/offer/plans_2-2.png"/></a>
    <br/>
    <a href="developer.webex.com">spark-java-sdk</a>
</h1>

[![license](https://img.shields.io/github/license/ciscospark/spark-java-sdk.svg)](https://github.com/ciscospark/spark-java-sdk/blob/master/LICENSE)

## Introduction

This Java SDK is a Java library for consuming Cisco Webex's RESTful APIs. Please visit us at https://developer.webex.com/ for more information about Cisco Webex for Developers.

_Why spark-java-sdk?_ : A rebranding took place in 2018, some time after this SDK was created, that renamed Cisco Spark to Cisco Webex.

## Prerequisites

- Java 1.8
- [Apache Maven](https://maven.apache.org/)

Clone the repository
```bash
git clone git@github.com:webex/spark-java-sdk.git
```
Run maven through CLI or your favourite IDE
```bash
mvn install
```

## Usage

To start, call the _Spark_ builder with your developer token (can be retrieved from https://developer.webex.com).
```java
String accessToken = "<<secret>>";

Spark spark = Spark.builder()
        .baseUrl(URI.create("https://api.ciscospark.com/v1"))
        .accessToken(accessToken)
        .build();
``` 

Work with Webex Teams rooms
```java
// List my rooms
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

// Share an adaptive card with the room
message = new Message();
message.setRoomId(room.getId());
message.setText("Mandatory fallback text");
JsonArray attachmentsArry = Json.createArrayBuilder() // Create an array to contain all the adaptive card JSONs
                .add(Json.createObjectBuilder() // Add the required key "contentType" which points to the fact that this attachment is of type adatpive card
                        .add("contentType", "application/vnd.microsoft.card.adaptive") // The content key will contain the actual card JSON generated from the adaptive cards designer
                        .add("content", Json.createObjectBuilder()
                                .add("$schema", "http://adaptivecards.io/schemas/adaptive-card.json")
                                .add("type", "AdaptiveCard")
                                .add("version", "1.0")
                                .add("body", Json.createArrayBuilder() // Create the initital body object of the card
                                        .add(Json.createObjectBuilder() // Create an object/element inside the body
                                                .add("type", "TextBlock") // This is an example of TextBlock element
                                                .add("text", "Here is a ninja cat")
                                                .build() // Build the object
                                        )
                                        .add(Json.createObjectBuilder()
                                                .add("type", "Image") // This is an example of Image element
                                                .add("url", "http://adaptivecards.io/content/cats/1.png")
                                                .build() // Build the image element
                                        )
                                        .build() // Build the body object
                                )
                                .build() // Build the card JSON
                        ))
                .build(); // Build the entire attachments array
message.setAttachments(attachmentsArry); // Set the attachments field on the message payload which has to be an array of JSON
spark.messages().post(message);

// Get person details
Person person=new Person();
person=spark.people().path("/<<<**Insert PersonId**>>>").get();
```
Connect to Webex Teams via webhooks
```java
// Create a new webhook
Webhook webhook = new Webhook();
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
```
Find all your relevant information through our APIs
```java
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
```
Work directly with your teams
```java
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

```

## Contributing

## Maintainers

- Brian (bbender)
- Santosh (santokum)

## License

&copy; 2018 Cisco Systems, Inc. and/or its affiliates. All Rights Reserved. See [LICENSE](LICENSE) for details.
