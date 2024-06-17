package hello.example.porthub.repository;

import hello.example.porthub.domain.*;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MentoRepository {
    private final SqlSessionTemplate sql;

    public int apply(MentoDto mentoDto) {
        return sql.insert("Mento.apply", mentoDto);
    }

    public int upload(MentoringDto mentoringDto) { return sql.insert("Mento.upload", mentoringDto); }

    public List<MentoViewDto> searchMentoring() { return sql.selectList("Mento.searchmentoring");}

    public List<MentoViewDto> searchMentoring(String searchString) { return sql.selectList("Mento.searchmentoring", searchString);}

    public List<MentoViewDto> searchMentoring(int CategoryID) {
        HashMap<String, Object> param= new HashMap<>();
        param.put("CategoryID",CategoryID);
        return sql.selectList("Mento.searchmentoring", param);
    }

    public List<MentoViewDto> searchMentoring(String searchString, int CategoryID) {
        HashMap<String, Object> param= new HashMap<>();
        param.put("searchString",searchString);
        param.put("CategoryID",CategoryID);
        return sql.selectList("Mento.searchmentoring", param);
    }

    public MentoViewDto SelectMentoView(int MentoringID){ return sql.selectOne("Mento.selectmentoview",MentoringID);}

    public MentoringDto mentoring(int MentoringID){return sql.selectOne("Mento.mentoring", MentoringID);}


    public String CheckMentoProcess(int MentoID){ return sql.selectOne("Mento.checkmentoprocess",MentoID);}

    public String PaidMentoringID(int UserID){return sql.selectOne("Mento.paidmentoringID",UserID);}

    public ActivityViewDto MentoringContent(int MentoringID){return sql.selectOne("Mento.mentoringcontent",MentoringID);}

    public List<MentoringDto> mymentoring(int MentoID){return sql.selectList("Mento.mymentoring",MentoID);}

    public int deletementoring(int MentoringID){return sql.update("Mento.deletementoring",MentoringID);}

    public MentoDto selectmento(int UserID){return sql.selectOne("Mento.selectmento", UserID);}

    public int updatemento(MentoDto mentoDto){return sql.update("Mento.updatemento",mentoDto);}

    public int newmentoprocess(MentoProcessDto mentoProcessDto){return sql.insert("Mento.newmentoprocess",mentoProcessDto);}

    public int updatementoprocess(MentoProcessDto mentoProcessDto){return sql.update("Mento.updatementoprocess",mentoProcessDto);}

    public int updatecredit(MentoDto mentoDto){return sql.update("Mento.updatecredit",mentoDto);}

}
