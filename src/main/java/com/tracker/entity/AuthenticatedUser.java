package com.tracker.entity;

/**
 * Represents an authenticated user extracted
 * from the Cognito JWT token claims.
 */
public class AuthenticatedUser {

    private final String sub;
    private final String email;
    private final String firstName;

    /**
     * Instantiates a new AuthenticatedUser.
     *
     * @param sub       the Cognito unique user ID
     * @param email     the user's email
     * @param firstName the user's first name
     */
    public AuthenticatedUser(String sub, String email, String firstName) {
        this.sub = sub;
        this.email = email;
        this.firstName = firstName;
    }

    public String getSub() { return sub; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
}