package com.sparta.bookflex.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UserRoleId.class)
public class UserRole {

    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }
        if(object == null || getClass() != object.getClass()) {
            return false;
        }
        UserRole userRole = (UserRole)object;
        return Objects.equals(user.getUsername(), userRole.getUser().getUsername()) &&
                Objects.equals(role.getRoleName(), userRole.getRole().getRoleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }
}

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
class UserRoleId implements Serializable {
    private Long user;
    private Long role;
}
