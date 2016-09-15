package com.ciscospark;

import java.net.URI;

public class Room {

    private String id;
    private String title;
    private String teamId;
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
