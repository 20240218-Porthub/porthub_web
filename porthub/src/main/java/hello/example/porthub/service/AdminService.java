package hello.example.porthub.service;


import hello.example.porthub.domain.CopyrightReportDto;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.MentoProcessDto;
import hello.example.porthub.repository.AdminRepository;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.MentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<MemberDto> AllUserList() {
        return adminRepository.AllUserList();
    }

    public String getReporterNameByEmail(String reporterEmail) {
        return adminRepository.findUserNameByEmail(reporterEmail);
    }

    public String getReportedNameById(int reportedID) {
        return adminRepository.findUserNameByID(reportedID);
    }
}
