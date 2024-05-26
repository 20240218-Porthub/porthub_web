package hello.example.porthub.config.util;

import hello.example.porthub.service.PortfolioService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final PortfolioService portfolioService;

    public ScheduledTasks(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void PopularTask() {
        portfolioService.updatePopularTask();
    }
}
