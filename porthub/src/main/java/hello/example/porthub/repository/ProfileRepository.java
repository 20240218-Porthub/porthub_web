package hello.example.porthub.repository;

import hello.example.porthub.domain.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProfileRepository {
    private final SqlSessionTemplate sql;

    public ProfileDto getUsrinfo(String Email) {
        return sql.selectOne("User.findByEmail", Email);
    }
}
