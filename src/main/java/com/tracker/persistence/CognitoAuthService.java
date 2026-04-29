package com.tracker.persistence;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Handles all AWS Cognito operations against the specific
 * User Pool. Responsible for registering, confirming,
 * and authenticating users.
 *
 * @author EmileM
 */
public class CognitoAuthService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final String clientId;
    private final String userPoolId;
    private Properties properties;

    /**
     * Instantiates a new CognitoAuthService. lastly,
     * initializes the appropriate variables.
     *
     * @param properties the read properties file context.
     * @param client the single shared cognito client.
     */
    public CognitoAuthService(Properties properties, CognitoIdentityProviderClient client) {
        this.properties = properties;
        this.cognitoClient = client;
        this.clientId = this.properties.getProperty("aws.cognito.clientId");
        this.userPoolId = this.properties.getProperty("aws.cognito.userPoolId");
    }

    /**
     * Computes the SECRET_HASH required for Cognito API requests
     * when the App Client is configured with a client Secret.
     *
     * @param username the user's email address used as the subject.
     * @return Base64 encoded SECRET_HASH string to be passed.
     * @throws Exception if the HmacSHA256 algorithm is unavailable or keys are invalid.
     */
    private String computeSecretHash(String username) throws Exception {
        String clientSecret = properties.getProperty("aws.cognito.clientSecret");

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(
                clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        mac.update(username.getBytes(StandardCharsets.UTF_8));
        byte[] rawHmac = mac.doFinal(clientId.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(rawHmac);
    }

    /**
     * Registers a new user in the Cognito User Pool.
     *
     * @param firstName the user's first name.
     * @param email the user's email.
     * @param password  the user's password
     *
     * @throws UsernameExistsException  If the email is already registered
     * @throws InvalidPasswordException   If the password doesn't meet policy requirements
     * @throws InvalidParameterException   If any required field is invalid or missing
     * @throws TooManyRequestsException  If too many requests are made in a short period
     */
    public String register(String firstName, String email, String password)
            throws UsernameExistsException, InvalidPasswordException,
            InvalidParameterException, TooManyRequestsException, Exception
    {

        AttributeType firstNameAttr = AttributeType.builder()
                .name("name")
                .value(firstName)
                .build();

        AttributeType emailAttr = AttributeType.builder()
                .name("email")
                .value(email)
                .build();

        SignUpRequest request = SignUpRequest.builder()
                .clientId(clientId)
                .username(email)
                .password(password)
                .secretHash(computeSecretHash(email))
                .userAttributes(firstNameAttr, emailAttr)
                .build();

        SignUpResponse response = cognitoClient.signUp(request);
        return response.userSub();
    }

    /**
     * Confirms a new user's registration using the
     * verification code sent to their email.
     *
     * @param email the user's email to confirm
     * @param code the verification code sent to the user's email
     *
     * @throws CodeMismatchException If the code that was sent to user doesn't match.
     * @throws ExpiredCodeException If the code expired
     */
    public void confirmSignUp(String email, String code) throws
            CodeMismatchException, ExpiredCodeException, Exception
    {

        ConfirmSignUpRequest request = ConfirmSignUpRequest.builder()
                .clientId(clientId)
                .username(email)
                .confirmationCode(code)
                .secretHash(computeSecretHash(email))
                .build();

        cognitoClient.confirmSignUp(request);
    }

    /**
     * Authenticates a user against the Cognito User Pool.
     *
     * @param email the user's email.
     * @param password the user's password.
     * @return AuthenticationResultType contains the tokens.
     *
     * Results should contain:
     * IdToken - who the user is.
     * accessToken - used for API calls.
     * refreshToken - used to get new token when expired.
     *
     * @throws NotAuthorizedException If the email or password is incorrect.
     * @throws UserNotConfirmedException If the user hasn't confirmed their email.
     * @throws UserNotFoundException If no account exists with that email.
     * @throws TooManyRequestsException If too many attempts are made.
     * @throws PasswordResetRequiredException If the user need to reset their password.
     */
    public AuthenticationResultType login(String email, String password)
            throws NotAuthorizedException, UserNotConfirmedException,
            UserNotFoundException, TooManyRequestsException,
            PasswordResetRequiredException, Exception
    {

        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", email);
        authParams.put("PASSWORD", password);
        authParams.put("SECRET_HASH", computeSecretHash(email));

        InitiateAuthRequest request = InitiateAuthRequest.builder()
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .clientId(clientId)
                .authParameters(authParams)
                .build();

        InitiateAuthResponse response = cognitoClient.initiateAuth(request);

        return response.authenticationResult();
    }

    /**
     * Initiates the forgot password flow by sending a reset code to the user's email.
     *
     * @param email the user's email address.
     *
     * @throws UserNotFoundException If no account exists with that email.
     * @throws InvalidParameterException If the email is invalid or the user is not confirmed.
     * @throws TooManyRequestsException If too many requests are made.
     */
    public void forgotPassword(String email)
            throws UserNotFoundException, InvalidParameterException,
            TooManyRequestsException, Exception
    {
        ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                .clientId(clientId)
                .username(email)
                .secretHash(computeSecretHash(email))
                .build();

        cognitoClient.forgotPassword(request);
    }

    /**
     * Confirms the forgot password flow by submitting the reset code and new password.
     *
     * @param email the user's email address.
     * @param code the verification code sent to the user's email.
     * @param newPassword the new password to set.
     *
     * @throws CodeMismatchException If the code does not match.
     * @throws ExpiredCodeException If the code has expired.
     * @throws InvalidPasswordException If the new password doesn't meet policy requirements.
     * @throws TooManyRequestsException If too many requests are made.
     */
    public void confirmForgotPassword(String email, String code, String newPassword)
            throws CodeMismatchException, ExpiredCodeException,
            InvalidPasswordException, TooManyRequestsException, Exception
    {
        ConfirmForgotPasswordRequest request = ConfirmForgotPasswordRequest.builder()
                .clientId(clientId)
                .username(email)
                .confirmationCode(code)
                .password(newPassword)
                .secretHash(computeSecretHash(email))
                .build();

        cognitoClient.confirmForgotPassword(request);
    }

    /**
     * Resends the confirmation code to the user's email.
     *
     * @param email the user's email address.
     *
     * @throws UserNotFoundException If no account exists with that email.
     * @throws TooManyRequestsException If too many requests are made.
     */
    public void resendConfirmationCode(String email)
            throws UserNotFoundException, TooManyRequestsException, Exception
    {
        ResendConfirmationCodeRequest request = ResendConfirmationCodeRequest.builder()
                .clientId(clientId)
                .username(email)
                .secretHash(computeSecretHash(email))
                .build();

        cognitoClient.resendConfirmationCode(request);
    }

    /**
     * Checks whether a user is fully confirmed in Cognito.
     * Used to detect the race condition where confirmSignUp internally succeeds
     * but returns ExpiredCodeException or NotAuthorizedException to the caller.
     *
     * @param email the user's email address (used as the Cognito username).
     * @return true if the user's status is CONFIRMED, false otherwise.
     * @throws Exception if the lookup fails.
     */
    public boolean isUserConfirmed(String email) throws Exception {
        AdminGetUserRequest request = AdminGetUserRequest.builder()
                .userPoolId(userPoolId)
                .username(email)
                .build();
        AdminGetUserResponse response = cognitoClient.adminGetUser(request);
        return UserStatusType.CONFIRMED.equals(response.userStatus());
    }

    /**
     * Permanently deletes a user from the Cognito User Pool.
     *
     * @param email the user's email address (used as the Cognito username).
     * @throws Exception if the deletion fails.
     */
    public void deleteUser(String email) throws Exception {
        AdminDeleteUserRequest request = AdminDeleteUserRequest.builder()
                .userPoolId(userPoolId)
                .username(email)
                .build();
        cognitoClient.adminDeleteUser(request);
    }
}