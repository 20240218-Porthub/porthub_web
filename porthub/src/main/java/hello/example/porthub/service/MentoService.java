package hello.example.porthub.service;

import hello.example.porthub.domain.MentoDto;
import hello.example.porthub.domain.MentoringDto;
import hello.example.porthub.domain.PortfolioDto;
import hello.example.porthub.repository.MentoRepository;
import hello.example.porthub.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MentoService {
    private final MentoRepository mentoRepository;
    public int apply(MentoDto mentoDto) {
        if(mentoRepository.apply(mentoDto)>0){
            return 1;
        }
        else{
            return 0;
        }
    }

    public int upload(MentoringDto mentoringDto) {
        if(mentoRepository.upload(mentoringDto)>0){
            return 1;
        }
        else{
            return 0;
        }
    }
}
