package hello.example.porthub.service;

import hello.example.porthub.domain.CategoryDto;
import hello.example.porthub.domain.PortfolioDto;
import hello.example.porthub.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;

    public int upload(PortfolioDto portfolioDto) {

        return 1;
    }

    public List<CategoryDto> findByCategory() {
        return portfolioRepository.findByCategory();
    }

}
