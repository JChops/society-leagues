package com.society.leagues.client.api;

public enum Role {
    ADMIN,
    PLAYER,
    ANON;

    public static boolean isAdmin(Role r) {
        return  r == ADMIN;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
