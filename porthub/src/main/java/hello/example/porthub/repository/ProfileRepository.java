package hello.example.porthub.repository;

import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.domain.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProfileRepository {
    private final SqlSessionTemplate sql;

    public int newmeta(int UserID){ return sql.insert("Member.newmeta", UserID);}

    public int metasave(ProfileDto profiledto){ return sql.update("Member.metaupdate", profiledto);}

    public ProfileDto findByUserID(int UserID){ return sql.selectOne("Member.findByUserID", UserID); }

    public List<MainPortViewDto> findportByUserID(int UserID, int pageSize, int offset){
        Map<String, Object> params = new HashMap<>();
        params.put("UserID", UserID);
        params.put("pageSize", pageSize);
        params.put("offset", offset);
        return sql.selectList("Portfolio.findPortsByUserID", params);
    }

    public List<MainPortViewDto> findAllPorts(String order, int pageSize, int offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("order", order);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return sql.selectList("Portfolio.findAllPorts", params);
    }

    public List<Integer> getUserFollowerListbyID(int userid) {
        return sql.selectList("Member.getUserFollowerListbyID", userid);
    }

    public List<Integer> getUserFollowingListbyID(int userid) {
        return sql.selectList("Member.getUserFollowingListbyID", userid);
    }

    public int countPortfoliosByUserID(int userid) {
        return sql.selectOne("Member.countPortfoliosByUserID", userid);
    }
}
