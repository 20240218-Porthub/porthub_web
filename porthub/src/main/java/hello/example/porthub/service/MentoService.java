package hello.example.porthub.service;

import hello.example.porthub.domain.MentoDto;
import hello.example.porthub.domain.MentoViewDto;
import hello.example.porthub.domain.MentoringDto;
import hello.example.porthub.domain.PortfolioDto;
import hello.example.porthub.repository.MentoRepository;
import hello.example.porthub.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
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

    public List<MentoViewDto> allmentoring(){
        return mentoRepository.allmentoring();
    }

    public MentoringDto mentoring(int MentoID){
        return mentoRepository.mentoring(MentoID);
    }

    public List<MentoViewDto> searchMentoring(String searchString){
        return mentoRepository.searchMentoring(searchString);
    }
}
