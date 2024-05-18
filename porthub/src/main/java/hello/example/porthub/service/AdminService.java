package hello.example.porthub.service;


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

    public List<MentoProcessDto> AllRequestMentoProcess(){return adminRepository.AllRequestMentoProcess();}

    public List<MentoProcessDto> AllMento(){return adminRepository.AllMento();}
}
