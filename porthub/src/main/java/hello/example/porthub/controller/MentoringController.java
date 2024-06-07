package hello.example.porthub.controller;

import hello.example.porthub.service.PaymentService;
import hello.example.porthub.service.PortfolioService;
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
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mentoring")
public class MentoringController {
    private final PaymentService paymentService;
    private final MemberRepository memberRepository;
    private final MentoService mentoService;
    private final PortfolioService portfolioService;
    private final S3Service s3Service;

    @GetMapping("/activity")
    public String MentorActivity(Principal principal, Model model){
        MemberDto member=memberRepository.findByEmail(principal.getName());
        int userid=member.getUserID();

        String mentoprocess=mentoService.CheckMentoProcess(userid);


        List<MentoringDto> myMentoring=mentoService.mymentoring(userid);
        String paidproducts=mentoService.PaidMentoringID(userid);
        List<ActivityViewDto> mentoringcontent=mentoService.MentoringContent(paidproducts);
        log.info("mymento="+myMentoring);


        model.addAttribute("mymentorings", myMentoring);
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
    public @ResponseBody Map loadMentoring(@RequestParam("MentoringID") int id, Principal principal){
        MentoringDto result=mentoService.mentoring(id);
        MemberDto member=memberRepository.findmemberByUserID(result.getMentoID());

        MemberDto currentusr=memberRepository.findByEmail(principal.getName());

        String usrpay=currentusr.getPaidProduct();

        Map<String,String> map= new HashMap<String, String>();

        if(currentusr.getUserID()==result.getMentoID()){
            map.put("MentoisMe","Y");
        }else{
            map.put("MentoisMe","N");
        }

        if(usrpay!=null){
            if(Arrays.asList(usrpay.split(",")).contains(Integer.toString(id))){
                map.put("alreadypay","Y");
            }else{
                map.put("alreadypay","N");
            }
        }else{
            map.put("alreadypay","N");
        }



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
    public @ResponseBody List searchMentoring(@RequestParam(value="searchString", required = false) String searchString,
                                              @RequestParam(value="CategoryName", required = false) String CategoryName){

        List<MentoViewDto> mentorings;
        int CategoryID = 0;

        log.info("param="+searchString+CategoryName);

        if(searchString!=null && CategoryName!=null){
            CategoryID= portfolioService.getCategoryID(CategoryName);
            mentorings=mentoService.searchMentoring(searchString,CategoryID);
        } else if (searchString==null && CategoryName!=null) {
            CategoryID= portfolioService.getCategoryID(CategoryName);
            mentorings=mentoService.searchMentoring(CategoryID);
        } else if (searchString!=null && CategoryName==null) {
            mentorings=mentoService.searchMentoring(searchString);
        } else {
            mentorings=mentoService.allmentoring();
        }

        log.info("categoryid="+CategoryID);

        return mentorings;
    }

    @PostMapping("/payment")
    public String paymentdata(@ModelAttribute MentoViewDto mentoViewDto, ModelMap modelMap, Principal principal){
        MemberDto member = memberRepository.findByEmail(principal.getName());
        MentoViewDto postdata = mentoService.SelectMentoView(mentoViewDto.getMentoringID());
        modelMap.addAttribute("buyer", member);
        modelMap.addAttribute("mentoring",postdata);
        return "mentoring/Payment";
    }

    @GetMapping("/payment/confirm/{id}")
    public String PaymentConfirm(Model model, @ModelAttribute OrderSaveDto orderSaveDto){
        orderSaveDto=paymentService.selectOrder(orderSaveDto.getOrderID());
        return "mentoring/paymentconfirm";
    }

    @PostMapping("/delete")
    public @ResponseBody String delete(@RequestParam("MentoringID") int id){
        mentoService.deletementoring(id);
        return "success";
    }

}
