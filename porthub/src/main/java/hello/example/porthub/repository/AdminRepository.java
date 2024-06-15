package hello.example.porthub.repository;


import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.MentoDto;
import hello.example.porthub.domain.AdminDto.PortAdminDto;
import hello.example.porthub.domain.AdminDto.UserAdminDto;
import hello.example.porthub.domain.CopyrightReportDto;
import hello.example.porthub.domain.MentoProcessDto;
import hello.example.porthub.domain.MentoringDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

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
  
    public List<CopyrightReportDto> AllCopyRightList() {
        return sql.selectList("admin.AllCopyRightList");
    }


    public String findUserNameByEmail(String reporterEmail) {
        return sql.selectOne("admin.findUserNameByEmail", reporterEmail);
    }

    public String findUserNameByID(int reportedID) {
        return sql.selectOne("admin.findUserNameByID", reportedID);
    }

    public List<PortAdminDto> findAllPortList() {
        return sql.selectList("admin.findAllPortList");
    }

    public List<UserAdminDto> AllUserList() {
        return sql.selectList("admin.AllUserList");
    }

    public List<MentoringDto> findAllMentoringList() {
        return sql.selectList("admin.findAllMentoringList");
    }

    public int UpdateState(int ReportID) {
        try {
            sql.update("admin.UpdateState", ReportID);
            return 1; // 성공 시 1 반환
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // 기타 예외 발생 시 0 반환
        }
    }

    public String findUserRoleByUserID(int userID) {
        return sql.selectOne("admin.findUserRoleByUserID", userID);
    }

    public void UserBanByUserID(int userID) {
        sql.update("admin.UserBanByUserID", userID);
    }

    public List<UserAdminDto> AllBannedUserList() {
        return sql.selectList("admin.AllBannedUserList");
    }

    public void UserLiftingbyUserID(int userID) {
        sql.update("admin.UserLiftingbyUserID", userID);
    }
}
