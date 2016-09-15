package com.ciscospark;

import java.net.URI;

public class Room {
    private String encryptionKeyConvTitleUrl;
    // TODO
    private String encryptionKeyUrl;
    private String id;
    private String kmsMessage;
    private URI kmsResourceObjectUrl;
    private String teamId;
    private String title;

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

    public String getEncryptionKeyConvTitleUrl() {
        return encryptionKeyConvTitleUrl;
    }

    public void setEncryptionKeyConvTitleUrl(String encryptionKeyConvTitleUrl) {
        this.encryptionKeyConvTitleUrl = encryptionKeyConvTitleUrl;
    }

    public String getEncryptionKeyUrl() {
        return encryptionKeyUrl;
    }

    public void setEncryptionKeyUrl(String encryptionKeyUrl) {
        this.encryptionKeyUrl = encryptionKeyUrl;
    }

    public String getKmsMessage() {
        return kmsMessage;
    }

    public void setKmsMessage(String kmsMessage) {
        this.kmsMessage = kmsMessage;
    }

    public URI getKmsResourceObjectUrl() {
        return kmsResourceObjectUrl;
    }

    public void setKmsResourceObjectUrl(URI kmsResourceObjectUrl) {
        this.kmsResourceObjectUrl = kmsResourceObjectUrl;
    }
}
