package com.ciscospark;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
class SparkImpl extends Spark {

    Client client;

    SparkImpl(Client client) {
        this.client = client;
    }

    @Override
    public RequestBuilder<Room> rooms() {
        return new RequestBuilderImpl(Room.class, client, "/rooms");
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
    public RequestBuilder<Person> people(){
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
        return new RequestBuilderImpl<Webhook>(Webhook.class, client, "/webhooks");
    }
}
