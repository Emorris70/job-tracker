package com.tracker.persistence;

import com.tracker.entity.AuthenticatedUser;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

import java.net.URL;
import java.util.Date;
import java.util.Properties;

/**
 * Verifies and extracts claims from the Cognito
 * issued JWT token. Ensures the token is valid,
 * signed, and not expired.
 *
 * @author EmileM
 */
public class TokenVerifier {
    private final String jwksUrl;

    /**
     * Instantiates a new TokenVerifier.
     * Builds the JWKS url from the User Pool region and ID.
     */
    public TokenVerifier(Properties properties) {
        String region = properties.getProperty("aws.cognito.region");
        String userPoolId = properties.getProperty("aws.cognito.userPoolId");

        this.jwksUrl = "https://cognito-idp." + region + ".amazonaws.com/"
                + userPoolId + "/.well-known/jwks.json";
    }

    /**
     * Verifies the JWT token signature and extracts
     * the user claims.
     *
     * @param idToken the token returned from Cognito after login.
     * @return AuthenticatedUser containing the extracted claims.
     * @throws Exception if the token is invalid, expired, or tampered with.
     */
    public AuthenticatedUser verify(String idToken) throws Exception {
        // Parse the raw token string
        SignedJWT signedJWT = SignedJWT.parse(idToken);

        // fetch Cognito's public keys from the JWKS url.
        JWKSet jwkSet = JWKSet.load(new URL(jwksUrl));

        // find the matching public key using the token's key ID (kid)
        RSAKey rsaKey = (RSAKey) jwkSet.getKeyByKeyId(
                signedJWT.getHeader().getKeyID()
        );

        if (rsaKey == null) {
            throw new Exception("Public key not found for token");
        }

        // verify the signature
        JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());

        if (!signedJWT.verify(verifier)) {
            throw new Exception("Token signature is invalid");
        }

        // Check token expiration
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime.before(new Date())) {
            throw new Exception("Token has expired");
        }

        // Extract claims and return AuthenticatedUser
        String sub = signedJWT.getJWTClaimsSet().getSubject();
        String email = signedJWT.getJWTClaimsSet().getStringClaim("email");
        String firstName = signedJWT.getJWTClaimsSet().getStringClaim("name");

        return new AuthenticatedUser(sub, email, firstName);
    }
}