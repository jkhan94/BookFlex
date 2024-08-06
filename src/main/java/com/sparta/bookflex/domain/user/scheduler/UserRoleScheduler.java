package com.sparta.bookflex.domain.user.scheduler;

import com.sparta.bookflex.domain.user.service.UserRoleScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "UserRoleScheduler")
@Component
@RequiredArgsConstructor
public class UserRoleScheduler {

    private final UserRoleScheduleService userRoleScheduleService;


    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void updateUserRole() {
        log.info("유저 등급 업데이트 실행");
        userRoleScheduleService.updateGrade();
    }
}
