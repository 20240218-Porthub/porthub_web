package hello.example.porthub.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB 사용
public class PortfolioRepositoryTest {

    @Autowired
    private PortfolioRepository repository;

    private static final int THREAD_COUNT = 10; // 스레드 개수를 대폭 늘림
    private static final int UPDATE_COUNT_PER_THREAD = 1000; // 각 스레드가 업데이트를 여러 번 수행

    @Test
    void updateViewsCount_highConcurrencyTest() throws InterruptedException {
        int portfolioID = 3;

        // 테스트 DB 초기화
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);


        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < UPDATE_COUNT_PER_THREAD; j++) {
                        repository.updateViewsCount(portfolioID); // 여러 번 호출
                        repository.portfolioIncreLikes(portfolioID);
                        if (j % 100 == 0) {
                            System.out.println("Current count (partial): " + repository.getViewsCount(portfolioID));
                        }
                        repository.portfolioDecreLikes(portfolioID);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드 완료 대기
        System.out.println("thread waiting complete ");
        executorService.shutdown();

        // 예상 결과 검증
        int expectedCount = THREAD_COUNT * UPDATE_COUNT_PER_THREAD;
        int actualCount = repository.getViewsCount(portfolioID);

        assertEquals(expectedCount, actualCount, "동시성 문제가 발생했습니다!");
    }
}
