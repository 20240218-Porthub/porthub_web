package hello.example.porthub.repository;


import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.MentoDto;
import hello.example.porthub.domain.MentoProcessDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminRepository {
    private final SqlSessionTemplate sql;

    public MentoProcessDto selectProcess(int ProcessID){return sql.selectOne("admin.selectprocess",ProcessID);}

    public int UpdateMentoInfo(MentoDto mentoDto){return sql.update("admin.UpdateMentoInfo",mentoDto);}

    public List<MentoDto> AllRequestMento(){return sql.selectList("admin.selectAllRequestMento");}

    public List<MentoProcessDto> AllMento(){return sql.selectList("admin.selectAllMento");}

    public int UpdateMentoProcess(MentoProcessDto mentoProcessDto){ return sql.update("admin.UpdateMentoProcess", mentoProcessDto);}

    public int DeleteMentoProcess(MentoProcessDto mentoProcessDto){ return sql.delete("admin.DeleteMentoProcess",mentoProcessDto);}

    public int setUserRole(MemberDto memberDto){return sql.update("admin.setUserRole",memberDto);}
}
