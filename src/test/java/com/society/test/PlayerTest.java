package com.society.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.ResponseBodyExtractionOptions;
import com.jayway.restassured.response.ValidatableResponse;
import com.society.leagues.Application;
import com.society.leagues.api.ApiController;
import com.society.leagues.api.player.PlayerDao;
import com.society.leagues.domain.DomainUser;
import com.society.leagues.domain.interfaces.Player;
import com.society.leagues.domain.player.PlayerDb;
import com.society.leagues.infrastructure.AuthenticatedExternalWebService;
import com.society.leagues.infrastructure.security.ExternalServiceAuthenticator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestConfig.class})
@WebAppConfiguration
@IntegrationTest(value = {"server.port:0"})
public class PlayerTest extends TestBase {

    @Test
    public void testPlayerTeamHistory() {
        String generatedToken = authenticate();
        Map<String,Object> playerInfo = new HashMap<>();
        playerInfo.put("player_id",1);
        playerInfo.put("league_name","some league name");
        when(mockPlayerDao.getTeamHistory(anyInt())).thenReturn(playerInfo);

        given().header(X_AUTH_TOKEN, generatedToken).
                when().post(ApiController.PLAYER_URL + "/teamHistory").
                then().statusCode(HttpStatus.OK.value());
        ResponseBodyExtractionOptions body =  given().header(X_AUTH_TOKEN, generatedToken).
                when().post(ApiController.PLAYER_URL + "/teamHistory").then().extract().body();
        assertNotNull(body);
        assertNotNull(body.jsonPath());
        JsonPath json = body.jsonPath();
        assertEquals(json.getInt("player_id"),playerInfo.get("player_id"));
        assertEquals(json.getString("league_name"),playerInfo.get("league_name"));
    }


}