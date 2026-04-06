package com.tracker.persistence;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import java.util.Properties;

/**
 * Builds and returns a single shared connection to AWS Cognito.
 * NOT the specific user pool
 *
 * @author EmileM
 */
public class CognitoClientUtil implements PropertiesLoader {
    private Properties properties;

    private static CognitoIdentityProviderClient cognitoClient;

    /**
     * Instantiates a new CognitoClientUtil
     */
    public CognitoClientUtil() {}

    /**
     * Instantiates a new CognitoClientUtil. And initializes
     * the properties variable.
     *
     * @param properties
     */
    public CognitoClientUtil(Properties properties) {
        this();
        this.properties = properties;
    }

    /**
     * Establishes and returns the shared Cognito client connection.
     *
     * @return the cognito client connection.
     */
    public CognitoIdentityProviderClient getClient() {
        if (cognitoClient == null) {
            String region = properties.getProperty("aws.cognito.region");

            cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(Region.of(region))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
        }
        return cognitoClient;
    }
}