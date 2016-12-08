package com.botmanat.config.app;

import com.google.api.client.util.Strings;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KeySingleton {

    @Getter
    private String pageAccessToken = null;

    @Getter
    private String webhookValidationToken = null;

    @Getter
    private String appTokenFromFBDevelopersPage = null;
    private static KeySingleton instance = null;

    private KeySingleton() {
        getSecrets();
    }

    private void getSecrets() {
        InputStream is = getClass().getResourceAsStream("/secrets.properties");

        Properties props = new Properties();
        try {
            props.load(is);
            this.pageAccessToken = props.getProperty("PAGE_ACCESS_TOKEN");
            this.appTokenFromFBDevelopersPage = props.getProperty("APP_TOKEN_FROM_FB_DEVELOPERS_PAGE");
            this.webhookValidationToken = props.getProperty("WEBHOOK_VALIDATION_TOKEN");

            if (Strings.isNullOrEmpty(this.pageAccessToken)
                    || Strings.isNullOrEmpty(this.webhookValidationToken)
                    || Strings.isNullOrEmpty(this.appTokenFromFBDevelopersPage)) {
                throw new RuntimeException("secrets.properties not configured properly!");
            }
        } catch (IOException e) {
            throw new RuntimeException("secrets.properties not configured properly! " + e.getMessage());
        }
    }

    public static KeySingleton getInstance() {
        if (instance == null) {
            instance = new KeySingleton();
        }

        return instance;

    }

}
