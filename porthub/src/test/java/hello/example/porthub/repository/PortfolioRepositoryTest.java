package hello.example.porthub.repository;

import hello.example.porthub.domain.CategoryDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioRepositoryTest {

    @Test
    void findByCategory() {

        PortfolioRepository portfolioRepository
        List<CategoryDto> categories = portfolioRepository.findByCategory();

        // then
        assertEquals(7, categories.size()); // 예상되는 카테고리 개수로 변경해주세요
        // 추가적인 검증 로직을 작성하세요
    }

}