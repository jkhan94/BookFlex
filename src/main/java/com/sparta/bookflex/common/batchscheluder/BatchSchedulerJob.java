package com.sparta.bookflex.common.batchscheluder;

import com.sparta.bookflex.domain.sale.service.SaleService;
import com.sparta.bookflex.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchSchedulerJob {

    private final SaleService saleService;
    private final UserService userService;


    @Bean
    public Job updateGradeJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws DuplicateJobException {
        Job job = new JobBuilder("updateGradeJob", jobRepository)
                .start(updateGradeStep(jobRepository, transactionManager))
                .build();
        return job;
    }

    public Step updateGradeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Step step = new StepBuilder("updateGradeStep", jobRepository)
                .tasklet(updateGradeJobTasklet(), transactionManager)
                .build();
        return step;
    }

    public Tasklet updateGradeJobTasklet() {
        return ((contribution, chunkContext) -> {
           log.info("유저 등급 업데이트 실행");
            userService.updateGrade();
            return RepeatStatus.FINISHED;
        });
    }

}