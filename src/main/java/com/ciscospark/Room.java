package com.ciscospark;

import java.net.URI;

public class Room {
    private String id;
    private String title;
    private String teamId;

    // TODO
    private String encryptionKeyUrl;
    private String encryptionKeyConvTitleUrl;
    private String kmsMessage;
    private URI kmsResourceObjectUrl;

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

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
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
