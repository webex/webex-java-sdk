package com.ciscospark;

import com.ciscospark.Message;

import java.net.URI;
import java.util.Date;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class WebhookEvent {
    private String id;
    private String name;
    private String resource;
    private String event;
    private String filter;
    private Message data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Message getData() {
        return data;
    }

    public void setData(Message message) {
        this.data = message;
    }

}
