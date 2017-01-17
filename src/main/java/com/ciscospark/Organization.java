package com.ciscospark;

import java.util.Date;

/**
 * Copyright (c) 2016 Cisco Systems, Inc. See LICENSE file.
 */
public class Organization {
    private String id;
    private String displayName;
    private Date created;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
