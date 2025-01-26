package hello.example.porthub.controller;

import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.repository.PortfolioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB 사용
class IndexControllerTest {

    @Autowired
    private PortfolioRepository repository;

    @Test
    @DisplayName("검색 쿼리 성능 비교 반복 테스트")
    void repeatedConcurrencyTest() throws InterruptedException {
        // 설정값
        int offset = (45 - 1) * 20; // 페이지 계산
        int threadCount = 10; // 동시에 실행할 쓰레드 개수
        int repeatCount = 10; // 반복 횟수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        long totalOriginalTime = 0; // 기존 쿼리 총 실행 시간
        long totalImprovedTime = 0; // 개선 쿼리 총 실행 시간
        long totalImprovedTime3 = 0; // 3번째 쿼리 총 실행 시간

        for (int repeat = 1; repeat <= repeatCount; repeat++) {
            System.out.println("반복 실행 #" + repeat);

            // 기존 쿼리 테스트
            CountDownLatch latch = new CountDownLatch(threadCount);
            long startTime1 = System.currentTimeMillis();

            for (int i = 0; i < threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        List<MainPortViewDto> results = repository.findAllSearchPorts("ViewsOrder", 20, offset, "modern", 3);
                        Assertions.assertThat(results).isNotNull(); // 결과가 null이 아님을 확인
                    } finally {
                        latch.countDown(); // 완료 시 카운트 감소
                    }
                });
            }
            latch.await(); // 모든 쓰레드가 완료될 때까지 대기
            long endTime1 = System.currentTimeMillis();

            long originalTime = endTime1 - startTime1;
            totalOriginalTime += originalTime;
            System.out.println("기존 쿼리 실행 시간: " + originalTime + "ms");

            // 성능 개선 쿼리 테스트
            CountDownLatch latch2 = new CountDownLatch(threadCount);
            long startTime2 = System.currentTimeMillis();

            for (int i = 0; i < threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        List<MainPortViewDto> results = repository.findAllSearchPorts2("ViewsOrder", 20, offset, "modern", 3);
                        Assertions.assertThat(results).isNotNull(); // 결과가 null이 아님을 확인
                    } finally {
                        latch2.countDown(); // 완료 시 카운트 감소
                    }
                });
            }
            latch2.await(); // 모든 쓰레드가 완료될 때까지 대기
            long endTime2 = System.currentTimeMillis();

            long improvedTime = endTime2 - startTime2;
            totalImprovedTime += improvedTime;
            System.out.println("성능 개선 쿼리 실행 시간: " + improvedTime + "ms");

            // 3번째 성능 개선 쿼리 테스트
            CountDownLatch latch3 = new CountDownLatch(threadCount);
            long startTime3 = System.currentTimeMillis();

            for (int i = 0; i < threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        List<MainPortViewDto> results = repository.findAllSearchPorts3("ViewsOrder", 20, offset, "modern", 3);
                        Assertions.assertThat(results).isNotNull(); // 결과가 null이 아님을 확인
                    } finally {
                        latch3.countDown(); // 완료 시 카운트 감소
                    }
                });
            }
            latch3.await(); // 모든 쓰레드가 완료될 때까지 대기
            long endTime3 = System.currentTimeMillis();

            long improvedTime3 = endTime3 - startTime3;
            totalImprovedTime3 += improvedTime3;
            System.out.println("3번째 성능 개선 쿼리 실행 시간: " + improvedTime3 + "ms");
        }

        executorService.shutdown(); // 쓰레드 풀 종료

        // 평균 실행 시간 출력
        System.out.println("반복 횟수: " + repeatCount);
        System.out.println("기존 쿼리 평균 실행 시간: " + (totalOriginalTime / repeatCount) + "ms");
        System.out.println("성능 개선 쿼리 평균 실행 시간: " + (totalImprovedTime / repeatCount) + "ms");
        System.out.println("3번째 성능 개선 쿼리 평균 실행 시간: " + (totalImprovedTime3 / repeatCount) + "ms");

        System.out.println("기존 쿼리 실행 계획:");
        Map<String, Object> params = new HashMap<>();
        params.put("order", "ViewsOrder");
        params.put("pageSize", 20);
        params.put("offset", offset);
        params.put("checkNum", 3);
        params.put("searchQuery", "modern");
        System.out.println(repository.findAllSearchPortsExplain(params));

        // 성능 개선된 쿼리 실행 계획
        System.out.println("성능 개선된 쿼리 실행 계획:");
        System.out.println(repository.findAllSearchPortsExplain2(params));

        // 3번째 성능 개선 쿼리 실행 계획
        System.out.println("3번째 성능 개선된 쿼리 실행 계획:");
        System.out.println(repository.findAllSearchPortsExplain3(params));

    }
}

