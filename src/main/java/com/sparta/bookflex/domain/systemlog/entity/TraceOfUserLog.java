package com.sparta.bookflex.domain.systemlog.entity;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.systemlog.enums.ActionType;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class TraceOfUserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType activityType;

    @Column
    private String description;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public TraceOfUserLog(ActionType action, String description, User user, Book book) {
        this.activityType = action;
        this.description = description;
        this.user = user;
        this.book = book;
    }
}
