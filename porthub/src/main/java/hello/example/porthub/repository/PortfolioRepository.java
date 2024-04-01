package hello.example.porthub.repository;


import hello.example.porthub.domain.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PortfolioRepository {
    private final SqlSessionTemplate sql;

    public List<CategoryDto> findByCategory() {
        return sql.selectList("Portfolio.findByCategory");
    }

}
