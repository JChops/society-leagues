package com.society.leagues.resource;

import com.society.leagues.client.api.admin.SeasonAdminApi;
import com.society.leagues.client.api.domain.Season;
import com.society.leagues.dao.SeasonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;

@Component
@RolesAllowed(value = {"ADMIN"})
@SuppressWarnings("unused")
public class SeasonAdminResource extends AdminApiResource implements SeasonAdminApi {
    private static Logger logger = LoggerFactory.getLogger(SeasonAdminResource.class);

    @Autowired SeasonDao dao;
    @Override
    public Season create(Season season) {
        if (season == null ||
                season.getName() == null ||
                season.getRounds() == null ||
                season.getStartDate() == null) {
            logger.error("Invalid Season: " + season);
            return null;
        }
        return dao.create(season);
    }

    @Override
    public Boolean delete(Season season) {
        if (season == null || season.getId() == null) {
            logger.error("Invalid Season: " + season);
            return null;
        }
        return dao.delete(season);
    }

    @Override
    public Season modify(Season season) {
        if (season ==  null || season.getId() == null || season.getName() == null) {
            logger.error("Invalid Season: " + season);
            return null;
        }
        return dao.modify(season);
    }
}
