package com.ciscospark;

import java.net.URI;
import java.util.Iterator;

/**
 * Created on 11/24/15.
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
    public RequestBuilder<Webhook> webhooks() {
        return new RequestBuilderImpl<>(Webhook.class, client, "/webhooks");
    }
}
