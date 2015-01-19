package com.society.test;

import com.society.leagues.client.ApiFactory;
import com.society.leagues.client.api.Role;
import com.society.leagues.client.api.admin.*;
import com.society.leagues.client.api.domain.*;
import com.society.leagues.client.api.domain.division.Division;
import com.society.leagues.client.api.domain.division.DivisionType;
import com.society.leagues.client.api.domain.division.LeagueType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SchemaData {
    public static boolean generated = false;
    public static String[] USERS = new String[]{"Doug_C", "Jeff_T", "Rob_P", "Doug_R", "Jared_P", "Howie_L", "Ken_S", "Alex_K",
                "Roger_P", "Saj_S", "Sofia_C", "Zack_Z", "Earl_S", "Efren_R", "John_S"};
    
    public static List<User> users = new ArrayList<>();
    
    public void generateData(String url , String token) {
        if (generated)
            return;
        
        SeasonAdminApi seasonApi = ApiFactory.createApi(SeasonAdminApi.class,token,url);
        DivisionAdminApi divisionApi  = ApiFactory.createApi(DivisionAdminApi.class,token,url);
        TeamAdminApi teamApi  = ApiFactory.createApi(TeamAdminApi.class,token,url);
        UserAdminApi userAdminApi  = ApiFactory.createApi(UserAdminApi.class,token,url);
        PlayerAdminApi  playerApi =  ApiFactory.createApi(PlayerAdminApi.class,token,url);
        
        Division eightBallChallenge = new Division(DivisionType.EIGHT_BALL_CHALLENGE, LeagueType.INDIVIDUAL);
        eightBallChallenge = divisionApi.create(eightBallChallenge);
        Division nineBallChallenge = new Division(DivisionType.NINE_BALL_CHALLENGE, LeagueType.INDIVIDUAL);
        nineBallChallenge = divisionApi.create(nineBallChallenge);

        Season eightBallSeason = new Season(eightBallChallenge, "8-ball Challenge ", new Date(), -1);
        eightBallSeason.setSeasonStatus(SeasonStatus.ACTIVE);
        eightBallSeason = seasonApi.create(eightBallSeason);
        Season nineBallSeason = new Season(nineBallChallenge, "9-ball Challenge ", new Date(), -1);
        nineBallSeason.setSeasonStatus(SeasonStatus.ACTIVE);
        nineBallSeason = seasonApi.create(eightBallSeason);

        String[] eighthandicap = new String[]{"3", "3", "3", "4", "4", "5", "5", "6", "6", "6", "7", "7", "7", "8", "8"};
        String[] ninehandicap = new String[]{"D", "D", "D", "D+", "D+", "C", "C", "C+", "C+", "C", "B", "B", "B+", "A", "A+"};

        for (int i = 0; i < USERS.length; i++) {
            String user = USERS[i];
            String[] name = user.split("_");
            User u = new User(name[0], name[1], user, user, Role.PLAYER);
            u = userAdminApi.create(u);
            users.add(u);
            Team team = new Team(user, eightBallChallenge);
            team = teamApi.create(team);

            Player player = new Player();
            player.setHandicap(eighthandicap[i]);
            player.setUser(u);
            player.setTeam(team);
            player.setStatus(Status.ACTIVE);
            player.setSeason(eightBallSeason);
            playerApi.create(player);
            
            team.setDivision(nineBallChallenge);
            team = teamApi.create(team);
            player = new Player();
            player.setHandicap(ninehandicap[i]);
            player.setUser(u);
            player.setTeam(team);
            player.setStatus(Status.ACTIVE);
            player.setSeason(nineBallSeason);
            playerApi.create(player);
        }
        generated = true;
    }
}