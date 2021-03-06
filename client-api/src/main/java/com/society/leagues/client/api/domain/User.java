package com.society.leagues.client.api.domain;

import com.society.leagues.client.api.domain.division.Division;

import javax.annotation.security.DenyAll;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("unused")
public class User extends LeagueObject {

    @NotNull
    String firstName;
    @NotNull
    String lastName;
    String email;
    String password;
    @NotNull
    String login;
    @NotNull
    Role role;

    Set<Division> divisions = new TreeSet<>();
    Set<Season> seasons = new TreeSet<>();
    Set<Team> teams = new TreeSet<>();
    Set<Player> players = new TreeSet<>();

    Set<Division> pastDivision = new TreeSet<>();
    Set<Season> pastSeasons = new TreeSet<>();
    Set<Team> pastTeams = new TreeSet<>();
    Set<Player> pastPlayers = new TreeSet<>();

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
    
    public User (Integer id) {
        setId(id);
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.role = Role.PLAYER;
    }

    public User() {

    }

    public User(String firstName, String lastName, String password, String login, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.login = login;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DenyAll
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void addRole(Role role) {
         this.role = role;
    }

    public boolean isAdmin() {
        return Role.isAdmin(role);
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> Teams) {
        this.teams = Teams;
    }

    public void addDivisions(List<Division> divisions) {
        if (this.divisions == null) {
            this.divisions = new TreeSet<>();
        }
        this.divisions.addAll(divisions);
    }

    public void addTeams(List<Team> teams) {
        if (this.teams == null) {
            this.teams = new TreeSet<>();
        }
        this.teams.addAll(teams);
    }

    public void addSeasons(List<Season> seasons) {
        if (this.seasons == null) {
            this.seasons = new TreeSet<>();
        }
        this.seasons.addAll(seasons);
    }

    public void addPlayers(List<Player> players) {
        if (this.players == null) {
            this.players = new TreeSet<>();
        }
        this.players.addAll(players);
    }
    
    public void addPlayer(Player player) {
        this.players.add(player);
    }


    public Set<Division> getPastDivision() {
        return pastDivision;
    }

    public void  addPastDivisions(List<Division> pastDivision) {
        this.pastDivision.addAll(pastDivision);
    }

    public Set<Season> getPastSeasons() {
        return pastSeasons;
    }

    public void addPastSeasons(List<Season> pastSeasons) {
        this.pastSeasons.addAll(pastSeasons);
    }

    public Set<Team> getPastTeams() {
        return pastTeams;
    }

    public void addPastTeams(List<Team> pastTeams) {
        this.pastTeams.addAll(pastTeams);
    }

    public Set<Player> getPastPlayers() {
        return pastPlayers;
    }

    public void addPastPlayers(List<Player> pastPlayers) {
        this.pastPlayers.addAll(pastPlayers);
    }

    public void setPastDivision(Set<Division> pastDivision) {
        this.pastDivision = pastDivision;
    }

    public void setPastSeasons(Set<Season> pastSeasons) {
        this.pastSeasons = pastSeasons;
    }

    public void setPastTeams(Set<Team> pastTeams) {
        this.pastTeams = pastTeams;
    }

    public void setPastPlayers(Set<Player> pastPlayers) {
        this.pastPlayers = pastPlayers;
    }

    public Set<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(Set<Division> Divisions) {
        this.divisions = Divisions;
    }

    public Set<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(Set<Season> Seasons) {
        this.seasons = Seasons;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> Players) {
        this.players = Players;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", role=" + role +
                '}';
    }
}
