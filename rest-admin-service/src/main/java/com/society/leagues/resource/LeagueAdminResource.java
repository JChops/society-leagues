package com.society.leagues.resource;

import com.society.leagues.client.admin.api.LeagueAdminApi;
import com.society.leagues.client.api.domain.league.League;
import com.society.leagues.dao.LeagueAdminDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;

@Component
@RolesAllowed(value = {"Root"})
@SuppressWarnings("unused")
public class LeagueAdminResource extends AdminApiResource implements LeagueAdminApi  {

    @Autowired
    LeagueAdminDao dao;
    private static Logger logger = LoggerFactory.getLogger(LeagueAdminResource.class);

    @Override
    public League create(League league) {
        if (league == null || league.getType() == null) {
            logger.error("League is not verified: "+ league);
            return null;
        }
        return dao.create(league);
    }

    @Override
    public Boolean delete(final League league) {
        return dao.delete(league);
    }

    @Override
    public League modify(League league) {
        if (league == null || league.getType() == null) {
            logger.error("League is not verified: "+ league);
            return null;
        }
        return dao.modify(league);
    }
}