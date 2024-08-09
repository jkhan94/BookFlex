package com.sparta.bookflex.common.utill;

public interface LogMessageFunction <T, V>{
    void apply(T main, V value);
}