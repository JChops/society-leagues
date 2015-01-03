package com.society.test;

import com.society.leagues.Main;

import com.society.leagues.client.admin.api.MatchResultApi;
import com.society.leagues.client.ApiFactory;
import com.society.leagues.client.api.Role;
import com.society.leagues.client.api.domain.User;
import com.society.leagues.client.exception.Unauthorized;
import com.society.leagues.client.api.domain.TokenResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.ProcessingException;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Main.class, TestConfig.class})
@IntegrationTest(value = {"server.port:0","daemon:true","debug:true"})
public class SecurityTest extends TestBase {

    @Test
    public void testAuth() {
        User user = new User();
        Mockito.when(mockedServiceAuthenticator.authenticate(NORMAL_USER,NORMAL_PASS)).
                thenReturn(user);

        TokenResponse response = authApi.authenticate(new User(NORMAL_USER, NORMAL_PASS));
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getToken());
    }

    @Test
    public void testFailure() {
        Mockito.when(mockedServiceAuthenticator.authenticate(NORMAL_USER, NORMAL_PASS)).
                thenThrow(new RuntimeException());

        TokenResponse response = authApi.authenticate(new User(NORMAL_USER, NORMAL_PASS));
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getToken());

        Mockito.reset(mockedServiceAuthenticator);
        Mockito.when(mockedServiceAuthenticator.authenticate(NORMAL_USER, NORMAL_PASS)).thenReturn(null);

        response = authApi.authenticate(new User(NORMAL_USER, NORMAL_PASS));
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getToken());
    }

    @Test
    public void testDenied() {
        try {
            MatchResultApi matchResultApi = ApiFactory.createApi(MatchResultApi.class, null, baseURL, true);
            matchResultApi.delete(0);
        } catch (ProcessingException e) {
            assertTrue(e.getCause() instanceof Unauthorized);
        }
    }

    @Test
    public void testAccess() {
        User user = new User();
        Mockito.when(mockedServiceAuthenticator.authenticate(
                ADMIN_USER,ADMIN_PASS))
                .thenReturn(user);

        String token = authenticate();
        MatchResultApi matchResultApi = ApiFactory.createApi(MatchResultApi.class, token, baseURL, true);
        assertNotNull(matchResultApi.delete(0));
    }
}