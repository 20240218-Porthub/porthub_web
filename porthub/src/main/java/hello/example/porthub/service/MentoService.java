package hello.example.porthub.service;

import hello.example.porthub.domain.*;
import hello.example.porthub.repository.MentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return mentoRepository.searchMentoring();
    }

    public MentoViewDto SelectMentoView(int MentoringID){
        return mentoRepository.SelectMentoView(MentoringID);
    }

    public MentoringDto mentoring(int MentoID){
        return mentoRepository.mentoring(MentoID);
    }

    public List<MentoViewDto> searchMentoring(String searchString){ return mentoRepository.searchMentoring(searchString);}

    public List<MentoViewDto> searchMentoring(int CategoryID){ return mentoRepository.searchMentoring(CategoryID);}

    public List<MentoViewDto> searchMentoring(String searchString,int CategoryID){ return mentoRepository.searchMentoring(searchString,CategoryID);}

    public String CheckMentoProcess(int UserID){return mentoRepository.CheckMentoProcess(UserID);}

    public String PaidMentoringID(int UserID){return mentoRepository.PaidMentoringID(UserID);}

    public List<ActivityViewDto> MentoringContent(String ids){
        String[] idArr = ids.split(",");
        List<ActivityViewDto> activityViewDtos=new ArrayList<ActivityViewDto>();
        for(int i =0; i<idArr.length; i++){
            ActivityViewDto temp=mentoRepository.MentoringContent(Integer.parseInt(idArr[i]));
            String[] urlarr=temp.getFile_urls().split(",");
            List<String> urllist=new ArrayList<String>();
            for(int j=0;j<urlarr.length; j++){
                urllist.add(urlarr[j]);
            }
            temp.setUrls(urllist);
            activityViewDtos.add(temp);
        }
        return activityViewDtos;
    }

    public List<MentoringDto> mymentoring(int MentoID){ return mentoRepository.mymentoring(MentoID);}

    public int deletementoring(int MentoringID){return mentoRepository.deletementoring(MentoringID);}

    public MentoDto selectmento(int UserID){return mentoRepository.selectmento(UserID);}

    public int updatemento(MentoDto mentoDto){return mentoRepository.updatemento(mentoDto);}

    public int newmentoprocess(MentoProcessDto mentoProcessDto){return mentoRepository.newmentoprocess(mentoProcessDto);}

    public int updatementoprocess(MentoProcessDto mentoProcessDto){return mentoRepository.updatementoprocess(mentoProcessDto);}

}
