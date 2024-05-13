package hello.example.porthub.repository;


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

    public List<MentoProcessDto> AllRequestMentoProcess(){return sql.selectList("admin.selectAllRequestMentoProcess");}

    public List<MentoProcessDto> AllMento(){return sql.selectList("admin.selectAllMento");}
}
