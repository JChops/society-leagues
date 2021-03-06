package com.society.test;

import com.society.leagues.Main;
import com.society.leagues.client.api.domain.Role;
import com.society.leagues.infrastructure.security.UserSecurityContext;
import com.society.leagues.infrastructure.token.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Main.class,TestBase.class})
@IntegrationTest(value = {"server.port:0","daemon:true","debug:true","embedded:true"})
public class TokenServiceTest extends TestBase {
    @Autowired TokenService tokenService;
    @Autowired JdbcTemplate jdbcTemplate;

    @Test
    public void testTokenService(){
        assertFalse(tokenService.contains("blah"));
        String token = authenticate(Role.ADMIN);

        assertTrue(tokenService.contains(token));
        assertNotNull(tokenService.retrieve(token));
        UserSecurityContext context = tokenService.retrieve(token);
        assertNotNull(context);
        assertNotNull(context.getUser());
        assertTrue(context.isUserInRole(Role.ADMIN.name()));

        token = authenticate(Role.PLAYER);
        context = tokenService.retrieve(token);
        assertNotNull(context);
        assertNotNull(context.getUser());
        assertFalse(context.isUserInRole(Role.ADMIN.name()));
        assertTrue(context.isUserInRole(Role.PLAYER.name()));

        //TODO Test with logout
        jdbcTemplate.update("update token_cache set created_date = '2000-01-01 00:00:00'");
        tokenService.evictExpiredTokens();
        assertFalse(tokenService.contains(token));
    }

    @Test
    public void testCache() {
        String token = authenticate(Role.ADMIN);
        tokenService.clearCache();
        assertTrue(tokenService.contains(token));
        tokenService.clearCache();
        UserSecurityContext context = tokenService.retrieve(token);
        assertNotNull(context);
        assertNotNull(context.getUser());
        assertTrue(context.isUserInRole(Role.ADMIN.name()));

        jdbcTemplate.update("delete from token_cache");
        tokenService.clearCache();
        assertFalse(tokenService.contains(token));
        assertNull(tokenService.retrieve(token));

    }
}
