package hello.example.porthub.repository;

import hello.example.porthub.domain.MentoDto;
import hello.example.porthub.domain.MentoringDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MentoRepository {
    private final SqlSessionTemplate sql;
    public int apply(MentoDto mentoDto) {
        return sql.insert("Mento.apply", mentoDto);
    }

    public int upload(MentoringDto mentoringDto) { return sql.insert("Mentoring.upload", mentoringDto); }
}
