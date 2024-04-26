package hello.example.porthub.repository;

import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.domain.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileRepository {
    private final SqlSessionTemplate sql;

    public int newmeta(int UserID){ return sql.insert("Member.newmeta", UserID);}

    public int metasave(ProfileDto profiledto){ return sql.update("Member.metaupdate", profiledto);}

    public ProfileDto findByUserID(int UserID){ return sql.selectOne("Member.findByUserID", UserID); }

    public List<MainPortViewDto> findportByUserID(int UserID){ return sql.selectList("Portfolio.findPortsByUserID", UserID);}
}
