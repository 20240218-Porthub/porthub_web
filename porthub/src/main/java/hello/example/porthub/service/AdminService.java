package hello.example.porthub.service;


import hello.example.porthub.domain.AdminDto.PortAdminDto;
import hello.example.porthub.domain.AdminDto.UserAdminDto;
import hello.example.porthub.domain.CopyrightReportDto;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.MentoProcessDto;
import hello.example.porthub.domain.MentoringDto;
import hello.example.porthub.repository.AdminRepository;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.MentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public List<MentoProcessDto> AllRequestMentoProcess(){return adminRepository.AllRequestMentoProcess();}

    public List<MentoProcessDto> AllMento(){return adminRepository.AllMento();}

    public int UpdateMentoProcess(MentoProcessDto mentoProcessDto){return adminRepository.UpdateMentoProcess(mentoProcessDto);}

    public int DeleteMentoProcess(MentoProcessDto mentoProcessDto){ return adminRepository.DeleteMentoProcess(mentoProcessDto);}

    public List<CopyrightReportDto> AllCopyRightList() {
        return adminRepository.AllCopyRightList();
    }

    public String getReporterNameByEmail(String reporterEmail) {
        return adminRepository.findUserNameByEmail(reporterEmail);
    }

    public String getReportedNameById(int reportedID) {
        return adminRepository.findUserNameByID(reportedID);
    }

    public List<PortAdminDto> AllPortList() {
        return adminRepository.findAllPortList();
    }

    public List<UserAdminDto> AllUserList() {
        return adminRepository.AllUserList();
    }

    public List<MentoringDto> AllMentoringList() {
        return adminRepository.findAllMentoringList();
    }

    public int UpdateState(int ReportID) {
        return adminRepository.UpdateState(ReportID);
    }

    public void UserBanbyUserID(int userID) {
        String getRole = adminRepository.findUserRoleByUserID(userID);

        if (getRole.equals("ADMIN")) {
            System.out.println("해당 유저는 관리자 입니다.");
        } else {
            adminRepository.UserBanByUserID(userID);
        }
    }

    public List<UserAdminDto> AllBannedUserList() {
        return adminRepository.AllBannedUserList();
    }

    public void UserLiftingbyUserID(int userID) {
        adminRepository.UserLiftingbyUserID(userID);
    }
}
