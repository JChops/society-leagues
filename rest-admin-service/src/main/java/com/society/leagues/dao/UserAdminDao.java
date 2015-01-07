package com.society.leagues.dao;

import com.society.leagues.client.api.admin.UserAdminApi;
import com.society.leagues.client.api.domain.User;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Component
public class UserAdminDao extends Dao implements UserAdminApi {

    @Override
    public User create(User user) {
        return create(user,getCreateStatement(user,CREATE));
    }

    @Override
    public Boolean delete(User user) {
        return delete(user,"UPDATE users set status = 0 where user_id = ?");
    }

    @Override
    public User modify(User user) {
        return modify(user,MODIFY,
                user.getLogin(),
                user.getRole().name(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    protected PreparedStatementCreator getCreateStatement(final User user, String sql) {
        return con -> {
            PreparedStatement ps = con.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setString(i++, user.getLogin());
            ps.setString(i++, user.getRole().name());
            ps.setString(i++, user.getFirstName());
            ps.setString(i++, user.getLastName());
            ps.setString(i++, user.getEmail());
            ps.setString(i, user.getPassword());

            return ps;
        };
    }

    static String CREATE = "INSERT INTO users " +
            "(" +
            "login," +
            "role," +
            "first_name," +
            "last_name," +
            "email," +
            "password) " +
            "VALUES " +
            "(?,?,?,?,?,?)";

    static String MODIFY = "UPDATE users " +
            "set " +
            "login=?," +
            "role=?," +
            "first_name=?," +
            "last_name=?," +
            "email=?," +
            "`password`= ? " +
            " where user_id = ?";
}
