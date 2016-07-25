package com.ciscospark;

import java.net.URI;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class Message {
    private String id;
    private String roomId;
    private String personId;
    private String personEmail;
    private String text;
    private String markdown;
    private String file;
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
    
    public String getMarkdown(){
    	return this.markdown;
    }
    
    public void setMarkdown(String markdown){
    	this.markdown=markdown;
    }
}
