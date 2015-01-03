package com.society.leagues.resource;

import com.society.leagues.client.api.AuthApi;
import com.society.leagues.client.api.domain.User;
import com.society.leagues.infrastructure.security.ServiceAuthenticator;
import com.society.leagues.client.api.domain.TokenResponse;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Api(value = "/auth",
        description = "Login to get token",
        position = 1,
        produces = "application/json")
@Component
public class AuthResource extends ApiResource implements AuthApi {
    @Autowired ServiceAuthenticator authenticator;

    @ApiOperation(value = "login",
            notes = "These fields can also be in the Header or Cookie of the request",
            response = TokenResponse.class)
    public TokenResponse authenticate (
            @ApiParam(required = true, defaultValue = "email_608@domain.com")
            String username,
            @ApiParam(required = true, defaultValue = "password_608")
            String password) {
        return auth(new User(username,password));
    }

    @ApiOperation(value = "login",
            notes = "These fields can also be in the Header or Cookie of the request",
            response = String.class)
    @Override
    public TokenResponse authenticate(
            @ApiParam(required = true) User user) {
         return auth(user);
    }

    private TokenResponse auth(User user) {
        TokenResponse response = new TokenResponse();
        response.setSuccess(false);
        try {
            User authUser = authenticator.authenticate(user.getUsername(),user.getPassword());
            if (authUser == null || authUser.getToken() == null)
                return response;

            response.setToken(authUser.getToken());
            response.setSuccess(true);
        } catch (Throwable ignore) {

        }
        return response;
    }
}
