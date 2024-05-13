package hello.example.porthub.repository;

import hello.example.porthub.domain.ActivityViewDto;
import hello.example.porthub.domain.MentoDto;
import hello.example.porthub.domain.MentoViewDto;
import hello.example.porthub.domain.MentoringDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MentoRepository {
    private final SqlSessionTemplate sql;

    public int apply(MentoDto mentoDto) {
        return sql.insert("Mento.apply", mentoDto);
    }

    public int upload(MentoringDto mentoringDto) { return sql.insert("Mento.upload", mentoringDto); }

    public List<MentoViewDto> allmentoring() { return sql.selectList("Mento.allmentoring");}

    public MentoViewDto SelectMentoView(int MentoringID){ return sql.selectOne("Mento.selectmentoview",MentoringID);}

    public MentoringDto mentoring(int MentoID){return sql.selectOne("Mento.mentoring", MentoID);}

    public List<MentoViewDto> searchMentoring(String searchString) { return sql.selectList("Mento.searchmentoring", searchString);}

    public String CheckMentoProcess(int MentoID){ return sql.selectOne("Mento.checkmentoprocess",MentoID);}

    public String PaidMentoringID(int UserID){return sql.selectOne("Mento.paidmentoringID",UserID);}

    public ActivityViewDto MentoringContent(int MentoringID){return sql.selectOne("Mento.mentoringcontent",MentoringID);}
}
