package com.society.leagues.dao;

import com.society.leagues.client.api.SeasonClientApi;
import com.society.leagues.client.api.admin.SeasonAdminApi;
import com.society.leagues.client.api.domain.Season;
import com.society.leagues.client.api.domain.Status;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Component
public class SeasonDao extends Dao<Season> implements SeasonClientApi,SeasonAdminApi {

    public static RowMapper<Season> rowMapper = (rs, rowNum) -> {
        Season season = new Season();
        season.setStartDate(rs.getDate("start_date"));
        season.setEndDate(rs.getDate("end_date"));
        season.setName(rs.getString("name"));
        season.setId(rs.getInt("season_id"));
        season.setRounds(rs.getInt("rounds"));
        season.setSeasonStatus(Status.valueOf(rs.getString("season_status")));
        return season;
    };

    @Override
    public Season create(Season season) {
        return create(season,getCreateStatement(season,CREATE));
    }

    @Override
    public Boolean delete(Season season) {
        return delete(season,"delete from season where season_id = ?");
    }

    @Override
    public Season modify(Season season) {
        return modify(season,
                "update season set name = ?, start_date = ?, end_date = ? , rounds = ? where season_id = ?"
                ,season.getName()
                ,season.getStartDate()
                ,season.getEndDate()
                ,season.getRounds()
                ,season.getId());
    }

    PreparedStatementCreator getCreateStatement(Season season, String sql) {
        return con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, season.getName());
            ps.setDate(2,new Date(season.getStartDate().getTime()));
            ps.setInt(3,season.getRounds());
            ps.setString(4,season.getSeasonStatus().name());
            return ps;
        };
    }

    final static String CREATE = "INSERT INTO season(name,start_date,rounds,season_status) VALUES (?,?,?,?)";

    @Override
    public String getSql() {
        return "select * from season";
    }

    @Override
    public RowMapper<Season> getRowMapper() {
        return rowMapper;
    }

    @Override
    public Season get(String name) {
        return get().stream().filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
