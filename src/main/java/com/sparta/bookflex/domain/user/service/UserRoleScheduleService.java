package com.sparta.bookflex.domain.user.service;

import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleScheduleService {

    private final UserService userService;

    public void updateGrade() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<User> userList = userService.getUsers();
        userList.stream()
                .filter(user -> !user.getSaleList().isEmpty())
                .forEach(user -> userService.updateUserRoleBySaleAmount(user.getId(), currentDateTime));
    }

}
