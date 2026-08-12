package org.vasya.planner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vasya.planner.service.NotificationService;
import org.vasya.planner.service.UserTaskService;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSchedulerService implements NotificationService {

    private final UserTaskService userTaskService;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    //@Scheduled(cron = "*/10 * * * * *")
    public void send() {
        log.info("scheduling");
        userTaskService.processAllUsers();
    }
}