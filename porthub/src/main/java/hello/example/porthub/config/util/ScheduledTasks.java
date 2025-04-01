package hello.example.porthub.config.util;

import hello.example.porthub.service.PortfolioService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final PortfolioService portfolioService;

    public ScheduledTasks(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @Scheduled(cron = "0 0 0 * * *")
//    @PostConstruct
    @Async
    public void PopularTask() {
//      정각 12시마다 업데이트
        portfolioService.updatePopularTask();
    }
}
