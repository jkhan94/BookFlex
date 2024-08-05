package com.sparta.bookflex.domain.user.dto;

import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserListResDto {
    private Long id;
    private LocalDateTime createdAt;
    private String username;
    private String name;
    private UserGrade grade;
    private String state;
    private BigDecimal purchaseTotal;

    @Builder
    public UserListResDto(Long id,
                          LocalDateTime createdAt,
                          String username,
                          String name,
                          UserGrade grade,
                          String state,
                          BigDecimal purchaseTotal) {
        this.id = id;
        this.createdAt = createdAt;
        this.username = username;
        this.name = name;
        this.grade = grade;
        this.state = state;
        this.purchaseTotal = purchaseTotal;
    }


}
