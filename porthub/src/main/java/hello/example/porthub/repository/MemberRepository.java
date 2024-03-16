package hello.example.porthub.repository;

import hello.example.porthub.domain.MemberDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

//mybatis 쿼리를 정의하고 호출하고 매개변수를 넘겨주고 등등의 역할

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final SqlSessionTemplate sql;

    public int save(MemberDto memberDto) {
        System.out.println("memberDto= " + memberDto);
//        return 0;
        return sql.insert("Member.save", memberDto);
    }
}
