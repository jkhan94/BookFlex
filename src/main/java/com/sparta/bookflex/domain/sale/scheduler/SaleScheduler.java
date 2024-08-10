package com.sparta.bookflex.domain.sale.scheduler;


import com.sparta.bookflex.domain.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaleScheduler implements ApplicationRunner {

    private final SaleService saleService;

    @Scheduled(cron = "0 0 0/3 * * *") //  매 3시간마다 체크후 적용
    public void updateIssueExpiredCoupon() throws InterruptedException {

        saleService.updateSaleStatus();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        updateIssueExpiredCoupon();
    }
}
