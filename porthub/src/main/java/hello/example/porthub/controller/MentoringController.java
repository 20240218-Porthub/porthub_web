package hello.example.porthub.controller;

import org.springframework.ui.Model;
import hello.example.porthub.domain.*;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import hello.example.porthub.service.MentoService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mentoring")
public class MentoringController {
    private final MemberRepository memberRepository;
    private final MentoService mentoService;
    private final S3Service s3Service;

    @GetMapping("/activity")
    public String MentorActivity(Principal principal, Model model){
        MemberDto member=memberRepository.findByEmail(principal.getName());
        int userid=member.getUserID();

        String mentoprocess=mentoService.CheckMentoProcess(userid);

        String paidproducts=mentoService.PaidMentoringID(userid);
        List<ActivityViewDto> mentoringcontent=mentoService.MentoringContent(paidproducts);
        log.info("mentoprocess="+mentoprocess);

        model.addAttribute("mentoprocess", mentoprocess);
        model.addAttribute("mentoringcontents", mentoringcontent);


        return "mentoring/activitymanage";
    }

    @GetMapping("/registermento")
    public String RegisterMento(){
        return "mentoring/registermento";
    }

    @PostMapping("/registermento/apply")
    public String MentoApply(@ModelAttribute MentoDto mentoDto, Principal principal) throws IOException {
        if(!mentoDto.getCareerCertification().isEmpty()) {
            String CareerUrl = s3Service.uploadFiles(mentoDto.getCareerCertification());
            mentoDto.setCareerUrl(CareerUrl);
        }
        if(!mentoDto.getUnivCertification().isEmpty()) {
            String UnivUrl = s3Service.uploadFiles(mentoDto.getUnivCertification());
            mentoDto.setUnivUrl(UnivUrl);
        }
        if(!mentoDto.getIssueCertification().isEmpty()) {
            String IssueUrl = s3Service.uploadFiles(mentoDto.getIssueCertification());
            mentoDto.setIssueUrl(IssueUrl);
        }

        String loginId = principal.getName();
        MemberDto member = memberRepository.findByEmail(loginId);

        mentoDto.setUserID(member.getUserID());


        int ApplyResult = mentoService.apply(mentoDto);
//        예제입니다.
        if (ApplyResult > 0) {
            return "redirect:/mentoring/activity"; //가입 성공
        } else {
            return "redirect:/mentoring/registermento"; //가입 실패
        }
    }

    @GetMapping("/createmento")
    public String CreateMento(){
        return "mentoring/createmento";
    }

    @PostMapping("/createmento/upload")
    public String uploadMentoring(@ModelAttribute MentoringDto mentoringDto, Principal principal) throws IOException {
        int cnt=0;
        mentoringDto.setThumbnail(s3Service.uploadFiles(mentoringDto.getThumbnailfile()));
        String fileurls=null;

        String loginId = principal.getName();
        MemberDto member = memberRepository.findByEmail(loginId);

        mentoringDto.setMentoID(member.getUserID());

        for (MultipartFile file : mentoringDto.getMentofile()) {
            if(cnt==1) {
                String multipleFile = s3Service.uploadFiles(file);
                fileurls = fileurls + ',' + multipleFile;
            }
            if(cnt==0) {
                String multipleFile = s3Service.uploadFiles(file);
                fileurls=multipleFile;
                cnt=1;
            }
        }
        mentoringDto.setFile_urls(fileurls);
        int uploadResult = mentoService.upload(mentoringDto);

        if (uploadResult > 0) {
            return "redirect:/mentoring";
        } else {
            return "redirect:/mentoring/createmento";
        }
    }

    @PostMapping("/load")
    public @ResponseBody Map loadMentoring(@RequestParam("MentoringID") int id){
        MentoringDto result=mentoService.mentoring(id);
        MemberDto member=memberRepository.findmemberByUserID(result.getMentoID());

        Map<String,String> map= new HashMap<String, String>();

        map.put("MentoringID",String.valueOf(id));
        map.put("MentoID",String.valueOf(result.getMentoID()));
        map.put("profileImage",member.getProfileImage());
        map.put("MentoName",member.getUserName());
        map.put("Title",result.getTitle());
        map.put("Contents",result.getContent());
        map.put("Price",String.valueOf(result.getPrice()));
        map.put("Thumbnail",result.getThumbnail());
        map.put("file_urls",result.getFile_urls());

        return map;
    }

    @PostMapping("/search")
    public @ResponseBody List searchMentoring(@RequestParam("searchString") String searchString){
        List<MentoViewDto> mentorings=mentoService.searchMentoring(searchString);
        return mentorings;
    }

    @PostMapping("/payment")
    public String paymentdata(@ModelAttribute MentoViewDto mentoViewDto, ModelMap modelMap){
        MentoViewDto postdata = mentoService.SelectMentoView(mentoViewDto.getMentoringID());
        modelMap.addAttribute("mentoring",postdata);
        return "mentoring/payment";
    }

}
