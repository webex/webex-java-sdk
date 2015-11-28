package com.ciscospark;

import java.net.URI;
import java.util.logging.Logger;

/**
 * Created on 11/24/15.
 */
public abstract class Spark {
    public abstract RequestBuilder<Room> rooms();
    public abstract RequestBuilder<Membership> memberships();
    public abstract RequestBuilder<Message> messages();
    public abstract RequestBuilder<Person> people();

    /**
     * Created on 11/24/15.
     */
    public interface Builder {
        Builder baseUrl(URI uri);
        Builder accessToken(String accessToken);
        Builder refreshToken(String refreshToken);
        Builder clientId(String clientId);
        Builder clientSecret(String clientSecret);
        Builder logger(Logger logger);
        Spark build();
    }

    public static Builder builder() {
        return new Builder() {
            private String accessToken;
            private String refreshToken;
            private String clientId;
            private String clientSecret;
            private Logger logger;
            private URI baseUrl = URI.create("https://api.ciscospark.com/v1");

            @Override
            public Builder baseUrl(URI uri) {
                this.baseUrl = uri;
                return this;
            }

            @Override
            public Builder accessToken(String accessToken) {
                this.accessToken = accessToken;
                return this;
            }

            @Override
            public Builder refreshToken(String refreshToken) {
                this.refreshToken = refreshToken;
                return this;
            }

            @Override
            public Builder clientId(String clientId) {
                this.clientId = clientId;
                return this;
            }

            @Override
            public Builder clientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
                return this;
            }

            @Override
            public Builder logger(Logger logger) {
                this.logger = logger;
                return this;
            }

            @Override
            public Spark build() {
                return new SparkImpl(new Client(baseUrl, accessToken, refreshToken, clientId, clientSecret, logger));
            }
        };
    }


}
