package hello.example.porthub.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB 사용
public class ConcurrencyTest {

    @Autowired
    private PortfolioRepository repository;

    @Test
    @DisplayName("동시성 테스트")
    void ConcurrencyTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for(int i = 0; i < 10; i++){
            executorService.execute(() -> {
                repository.updateViewsCount(3);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        Integer getCount = repository.getViewsCount(3);
        Assertions.assertThat(getCount).isEqualTo(10);

    }
}
