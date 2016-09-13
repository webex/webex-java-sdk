package com.ciscospark;

public class SparkEncryptionImpl extends Spark {

    private final Client client;
    private final KeyManager keyManager;

    public SparkEncryptionImpl(Client client) {
        this.client = client;
        this.keyManager = new KeyManager(client);
    }

    @Override
    public RequestBuilder<Room> rooms() {
        return new RequestBuilderRoomEncryptionImpl(Room.class, client, "/rooms", keyManager);
    }

    @Override
    public RequestBuilder<Membership> memberships() {
        return new RequestBuilderImpl(Membership.class, client, "/memberships");
    }

    @Override
    public RequestBuilder<Message> messages() {
        return new RequestBuilderImpl(Message.class, client, "/messages");
    }

    @Override
    public RequestBuilder<Person> people() {
        return new RequestBuilderImpl(Person.class, client, "/people");
    }

    @Override
    public RequestBuilder<Team> teams() {
        return new RequestBuilderImpl(Team.class, client, "/teams");
    }

    @Override
    public RequestBuilder<TeamMembership> teamMemberships() {
        return new RequestBuilderImpl(TeamMembership.class, client, "/team/memberships");
    }

    @Override
    public RequestBuilder<Webhook> webhooks() {
        return new RequestBuilderImpl<>(Webhook.class, client, "/webhooks");
    }
}
