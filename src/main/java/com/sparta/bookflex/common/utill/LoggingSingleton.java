package com.sparta.bookflex.common.utill;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.systemlog.entity.SystemLog;
import com.sparta.bookflex.domain.systemlog.entity.TraceOfUserLog;
import com.sparta.bookflex.domain.systemlog.enums.ActionType;
import com.sparta.bookflex.domain.user.entity.User;

public class LoggingSingleton {

    private LoggingSingleton(){};

    private static class SingletonGetter {
        private static final LoggingSingleton INSTANCE = new LoggingSingleton();
    }

    public static LoggingSingleton getInstance(){
        return SingletonGetter.INSTANCE;
    }

    public static SystemLog Logging(ActionType type, User user, String name, long value) {
        type.setTarget(name, value, type);
        String description = type.getDescriptionMsg();

        SystemLog log = new SystemLog(type,description, user);
        return log;
    }

    public static TraceOfUserLog userLogging(ActionType type, User user, String name, long value, Book book) {
        type.setTarget(name, value, type);
        String description = type.getDescriptionMsg();
        TraceOfUserLog userLog = new TraceOfUserLog(type, description, user, book);
        return userLog;
    }
}
