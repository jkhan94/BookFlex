//package com.sparta.bookflex.domain.sale.salescheduler;
//
//import com.sparta.bookflex.domain.user.service.UserRoleScheduleService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.DuplicateJobException;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class RecommandJob {
//
//    private final UserRoleScheduleService userRoleScheduleService;
//
//
//    @Bean
//    public Job testJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws DuplicateJobException {
//        Job job = new JobBuilder("testJob", jobRepository)
//                .start(testStep(jobRepository, transactionManager))
//                .build();
//        return job;
//    }
//
//    public Step testStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        Step step = new StepBuilder("testStep", jobRepository)
//                .tasklet(testTasklet(), transactionManager)
//                .build();
//        return step;
//    }
//
//    public Tasklet testTasklet() {
//        return ((contribution, chunkContext) -> {
//            System.out.println("***** hello batch! *****");
//            userRoleScheduleService.updateGrade();
//            return RepeatStatus.FINISHED;
//        });
//    }
//
//}