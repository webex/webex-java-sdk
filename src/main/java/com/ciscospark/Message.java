package com.ciscospark;

import java.net.URI;
import java.util.Date;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class Message {
    private String id;
    private String roomId;
    private String personId;
    private String personEmail;
    private String text;
    private String file;
    private String roomType;
    private Date created;
    private URI[] files;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public URI[] getFiles() {
        return files;
    }

    public void setFiles(URI... files) {
        this.files = files;
    }

    public Date getCreated() {
	return created;
    }
 
    public void setCreated(Date created) {
	this.created = created;
    }

    public String getRoomType() {
        return roomType;
    } 
    
    public void setRoomType(String roomType) {
	this.roomType = roomType;
    }
}
