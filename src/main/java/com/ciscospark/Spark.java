package com.ciscospark;

import java.net.URI;
import java.util.logging.Logger;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public abstract class Spark {
    public abstract RequestBuilder<Room> rooms();
    public abstract RequestBuilder<Membership> memberships();
    public abstract RequestBuilder<Message> messages();
    public abstract RequestBuilder<Person> people();
    public abstract RequestBuilder<Team> teams();
    public abstract RequestBuilder<TeamMembership> teamMemberships();
    public abstract RequestBuilder<Webhook> webhooks();
    public abstract RequestBuilder<Organization> organizations();
    public abstract RequestBuilder<License> licenses();
    public abstract RequestBuilder<Role> roles();


    /**
     * Created on 11/24/15.
     */
    public static class Builder {
        private URI redirectUri;
        private String authCode;
        private String accessToken;
        private String refreshToken;
        private String clientId;
        private String clientSecret;
        private Logger logger;
        private URI baseUrl = URI.create("https://api.ciscospark.com/v1");

        public Builder baseUrl(URI uri) {
            this.baseUrl = uri;
            return this;
        }

        public Builder redirectUri(URI uri) {
            this.redirectUri = uri;
            return this;
        }

        public Builder authCode(String code) {
            this.authCode = code;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Spark build() {
            return new SparkImpl(new Client(baseUrl, authCode, redirectUri, accessToken, refreshToken, clientId, clientSecret, logger));
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
