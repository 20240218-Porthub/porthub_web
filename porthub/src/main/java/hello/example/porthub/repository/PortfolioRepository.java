package hello.example.porthub.repository;


import hello.example.porthub.domain.CategoryDto;
import hello.example.porthub.domain.ImagesDto;
import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.domain.PortfolioDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class PortfolioRepository {

//    private static final Map<Long, PortfolioDto> portfolioItem = new ConcurrentHashMap<>();
//    //동시성 문제 해결하기 위함 ConcurrntHashMap, AtomicLong
//    private static final AtomicLong sequence = new AtomicLong(0);



    private final SqlSessionTemplate sql;

    public List<CategoryDto> findByCategory() {
        return sql.selectList("Portfolio.findByCategory");
    }


    public void PortUpload(PortfolioDto portfolioDto) {
        sql.insert("Portfolio.insertPortfolio", portfolioDto);
    }

    public void ContentUpload(ImagesDto imagesDto) {
        sql.insert("Portfolio.insertPortImages", imagesDto);
    }

    public int PortID() {
        return sql.selectOne("Portfolio.selectPortfolioID");
    }

    public List<MainPortViewDto> findAllPorts() {
        return sql.selectList("Portfolio.findAllPorts");
    }
}
