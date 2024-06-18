package hello.example.porthub.repository;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

//mybatis 쿼리를 정의하고 호출하고 매개변수를 넘겨주고 등등의 역할

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final SqlSessionTemplate sql;


    public int save(MemberDto memberDto) {
        return sql.insert("Member.save", memberDto);
    }

    public int imagesave(MemberDto memberDto){
        return sql.update("Member.imageupdate",memberDto);
    }

    public MemberDto findByUserName(String UserName) {
        return sql.selectOne("Member.findByUserName", UserName);
    }

    public MemberDto findByEmail(String Email) {
        return sql.selectOne("Member.findByEmail", Email);
    }

    public MemberDto findmemberByUserID(int UserID){ return sql.selectOne("Member.findmemberByUserID", UserID); }

    public MemberDto findByUserIDtoEmail(String Email) {
        return sql.selectOne("Member.findByUserIDtoEmail", Email);
    }

}
