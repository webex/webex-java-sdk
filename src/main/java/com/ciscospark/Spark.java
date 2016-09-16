package com.ciscospark;

import java.net.URI;
import java.util.logging.Logger;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public abstract class Spark {

    public static Builder builder() {
        return new Builder();
    }

    public abstract RequestBuilder<Room> rooms();

    public abstract RequestBuilder<Membership> memberships();

    public abstract RequestBuilder<Message> messages();

    public abstract RequestBuilder<Person> people();

    public abstract RequestBuilder<Team> teams();

    public abstract RequestBuilder<TeamMembership> teamMemberships();

    public abstract RequestBuilder<Webhook> webhooks();

    public static class Builder {
        private String accessToken;
        private String authCode;
        private URI baseUrl = URI.create("https://api.ciscospark.com/v1");
        private String clientId;
        private String clientSecret;
        private Boolean enableEndToEndEncryption;
        private Logger logger;
        private URI redirectUri;
        private String refreshToken;

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

        public Builder enableEndToEndEncryption(Boolean enableEndToEndEncryption) {
            this.enableEndToEndEncryption = enableEndToEndEncryption;
            return this;
        }

        public Spark build() {
            Client client = new Client(baseUrl, authCode, redirectUri, accessToken, refreshToken, clientId, clientSecret, logger, enableEndToEndEncryption);
            if (enableEndToEndEncryption) {
                return new SparkEncryptionImpl(client);
            } else {
                return new SparkImpl(client);
            }
        }
    }
}
