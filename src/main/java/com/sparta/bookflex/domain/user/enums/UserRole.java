package com.sparta.bookflex.domain.user.enums;

public enum UserRole {
    USER(UserType.USER),
    ADMIN(UserType.ADMIN);

    private final String userAuth;

    UserRole(String userAuth) { this.userAuth = userAuth;}

    public String getAuth() { return userAuth;}

    private static class UserType{
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
    }
}