package com.sparta.bookflex.domain.user.enums;

public enum UserState {
    BAN(State.BAN_STATE),
    ACTIVE(State.ACTIVE_STATE),
    DELETE(State.DELETE_STATE);

    private String state;
    UserState(String state){
    }

    public String getStateString(){
        return state;
    }

    private static class State {
        public static final String BAN_STATE = "접근 금지 상태입니다";
        public static final String ACTIVE_STATE = "활성화 상태입니다";
        public static final String DELETE_STATE = "회원탈퇴 상태입니다";
    }
}