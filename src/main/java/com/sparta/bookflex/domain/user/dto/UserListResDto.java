package com.sparta.bookflex.domain.user.dto;

import com.sparta.bookflex.domain.user.enums.UserGrade;
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
    private BigDecimal purchaseTotal;

    @Builder
    public UserListResDto(Long id,
                          LocalDateTime createdAt,
                          String username,
                          String name,
                          UserGrade grade,
                          BigDecimal purchaseTotal) {
        this.id = id;
        this.createdAt = createdAt;
        this.username = username;
        this.name = name;
        this.grade = grade;
        this.purchaseTotal = purchaseTotal;
    }


}
