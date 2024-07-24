package com.sparta.bookflex.domain.systemlog.entity;

import com.sparta.bookflex.domain.systemlog.ActionType;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ActionType action;

    @Column
    private String description;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
}
