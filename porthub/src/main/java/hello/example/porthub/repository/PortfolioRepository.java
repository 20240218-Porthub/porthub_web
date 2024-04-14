package hello.example.porthub.repository;


import hello.example.porthub.domain.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public PortViewDto findportview(int portfolioID) {
        return sql.selectOne("Portfolio.findPortfolioByPortfolioID", portfolioID);
    }

    public List<ImagesDto> findFileviews(int portfolioID) {
        return sql.selectList("Portfolio.findPortFilesByPortfolioID", portfolioID);
    }

    public List<PortViewDto> finduserport(int portfolioID) {
        return sql.selectList("Portfolio.finduserport", portfolioID);
    }

    public void insertCopyRightReportDto(CopyrightReportDto copyrightReportDto) {
        sql.insert("Portfolio.insertCopyrightDto", copyrightReportDto);
    }
}
