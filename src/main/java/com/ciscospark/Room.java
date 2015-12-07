package com.ciscospark;

import java.util.Date;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class Room {
    private String id;
    private String title;
    private Date created;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
