package hello.example.porthub.controller;


import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.MentoDto;
import hello.example.porthub.domain.MentoProcessDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.service.AdminService;
import hello.example.porthub.service.MentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final MentoService mentoService;
    private final MemberRepository memberRepository;
    @GetMapping("")
    public String admin() {
        return "adm/admin";
    }

    @GetMapping("/mento")
    public String AdminMento(Model model){
        List<MentoDto> requestmento = adminService.AllRequestMento();
        List<MentoProcessDto> allmentoprocess = adminService.AllMento();

        log.info("request="+requestmento+", allmento="+allmentoprocess);

        model.addAttribute("requestmentos",requestmento);
        model.addAttribute("allmento",allmentoprocess);

        return "adm/admin_mento";
    }

    @PostMapping("/mento/{status}/{id}")
    @ResponseBody
    public MentoProcessDto AcceptMento(@PathVariable String status, @PathVariable int id, @RequestParam("company") String company, @RequestParam("univ") String univ, @RequestParam("issue") String issue){
        MentoProcessDto mentoProcessDto = adminService.selectProcess(id);
        MentoDto mentoDto=mentoService.selectmento(mentoProcessDto.getMentoID());
        MemberDto memberDto=memberRepository.findmemberByUserID(mentoDto.getUserID());

        log.info("company="+company+",univ="+univ+",issue="+issue);

        if(Objects.equals(status, "accept")){
            if(!company.isBlank()){
                mentoDto.setCompanyName(company);
            }
            if(!univ.isBlank()){
                mentoDto.setUnivName(univ);
            }
            if(!issue.isBlank()){
                mentoDto.setCertificationName(issue);
            }

            mentoProcessDto.setProcess("1");

            memberDto.setRole("MENTO");

            adminService.setUserRole(memberDto);
            adminService.UpdateMentoInfo(mentoDto);
            adminService.UpdateMentoProcess(mentoProcessDto);


        }
        if(Objects.equals(status, "deny")){
            mentoProcessDto.setProcess("2");
            adminService.UpdateMentoProcess(mentoProcessDto);
        }
        if(Objects.equals(status, "delete")){;
            adminService.DeleteMentoProcess(mentoProcessDto);
        }

        return mentoProcessDto;
    }


}
