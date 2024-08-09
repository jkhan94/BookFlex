package com.sparta.bookflex.domain.user.enums;

public enum UserState {
    BAN(State.BAN_STATE),
    ACTIVE(State.ACTIVE_STATE),
    WITHDRAW(State.WITHDRAW);

    private final String state;
    UserState(String state){
        this.state = state;
    }

    public String getStateString(){
        return state;
    }

    private static class State {
        public static final String BAN_STATE = "밴";
        public static final String ACTIVE_STATE = "정상";
        public static final String WITHDRAW = "회원탈퇴";
    }
}