package com.tracker_test.entity;

import com.tracker.entity.AuthenticatedUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link AuthenticatedUser}.
 */
class AuthenticatedUserTest {

    /** Constructor should store sub, email, and firstName correctly. */
    @Test
    void constructor_setsAllFields() {
        AuthenticatedUser user = new AuthenticatedUser("sub-xyz", "john@example.com", "John");

        assertAll(
                () -> assertEquals("sub-xyz", user.getSub()),
                () -> assertEquals("john@example.com", user.getEmail()),
                () -> assertEquals("John", user.getFirstName())
        );
    }

    /** accessToken should be null until explicitly set. */
    @Test
    void accessToken_isNullByDefault() {
        AuthenticatedUser user = new AuthenticatedUser("sub-xyz", "john@example.com", "John");
        assertNull(user.getAccessToken());
    }

    /** setAccessToken should update the token value. */
    @Test
    void setAccessToken_updatesToken() {
        AuthenticatedUser user = new AuthenticatedUser("sub-xyz", "john@example.com", "John");
        user.setAccessToken("eyJhbGciOiJSUzI1NiJ9.token");

        assertEquals("eyJhbGciOiJSUzI1NiJ9.token", user.getAccessToken());
    }

    /** Two instances with the same fields should be equal (Lombok @Data). */
    @Test
    void equals_sameFields_returnsTrue() {
        AuthenticatedUser a = new AuthenticatedUser("sub-xyz", "john@example.com", "John");
        AuthenticatedUser b = new AuthenticatedUser("sub-xyz", "john@example.com", "John");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    /** Two instances with different subs should not be equal. */
    @Test
    void equals_differentSub_returnsFalse() {
        AuthenticatedUser a = new AuthenticatedUser("sub-001", "john@example.com", "John");
        AuthenticatedUser b = new AuthenticatedUser("sub-002", "john@example.com", "John");

        assertNotEquals(a, b);
    }
}
