package hello.example.porthub.service;


import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.MentoDto;
import hello.example.porthub.domain.MentoProcessDto;
import hello.example.porthub.repository.AdminRepository;
import hello.example.porthub.repository.MentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public MentoProcessDto selectProcess(int ProcessID){return adminRepository.selectProcess(ProcessID);}

    public int UpdateMentoInfo(MentoDto mentoDto){return adminRepository.UpdateMentoInfo(mentoDto);}

    public List<MentoDto> AllRequestMento(){return adminRepository.AllRequestMento();}

    public List<MentoProcessDto> AllMento(){return adminRepository.AllMento();}

    public int UpdateMentoProcess(MentoProcessDto mentoProcessDto){return adminRepository.UpdateMentoProcess(mentoProcessDto);}

    public int DeleteMentoProcess(MentoProcessDto mentoProcessDto){ return adminRepository.DeleteMentoProcess(mentoProcessDto);}

    public int setUserRole(MemberDto memberDto){return adminRepository.setUserRole(memberDto);}
}
