package com.ciscospark;

import java.net.URI;
import java.util.Date;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class Room {

    private String id;
    private String title;
    private String teamId;
    private Boolean isLocked;
    private Date created;
    private Date lastActivity;
    private String encryptionKeyUrl;
    private String titleEncryptionKeyUrl;
    private String keyManagementMessage;
    private URI keyManagementResourceObjectUrl;

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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getTitleEncryptionKeyUrl() {
        return titleEncryptionKeyUrl;
    }

    public void setTitleEncryptionKeyUrl(String titleEncryptionKeyUrl) {
        this.titleEncryptionKeyUrl = titleEncryptionKeyUrl;
    }

    public String getEncryptionKeyUrl() {
        return encryptionKeyUrl;
    }

    public void setEncryptionKeyUrl(String encryptionKeyUrl) {
        this.encryptionKeyUrl = encryptionKeyUrl;
    }

    public String getKeyManagementMessage() {
        return keyManagementMessage;
    }

    public void setKeyManagementMessage(String keyManagementMessage) {
        this.keyManagementMessage = keyManagementMessage;
    }

    public URI getKeyManagementResourceObjectUrl() {
        return keyManagementResourceObjectUrl;
    }

    public void setKeyManagementResourceObjectUrl(URI keyManagementResourceObjectUrl) {
        this.keyManagementResourceObjectUrl = keyManagementResourceObjectUrl;
    }
}
