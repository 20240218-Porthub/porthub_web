package hello.example.porthub.controller;


import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.MentoDto;
import hello.example.porthub.domain.MentoProcessDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.service.AdminService;
import hello.example.porthub.service.MentoService;
import hello.example.porthub.config.util.SessionUtils;
import hello.example.porthub.domain.*;
import hello.example.porthub.domain.AdminDto.PortAdminDto;
import hello.example.porthub.domain.AdminDto.UserAdminDto;
import hello.example.porthub.service.AdminService;
import hello.example.porthub.service.PortfolioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final PortfolioService portfolio2;

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
    public MentoProcessDto AcceptMento(@PathVariable("status") String status, @PathVariable("id") int id, @RequestParam("company") String company, @RequestParam("univ") String univ, @RequestParam("issue") String issue){
        log.info("status="+status);
        log.info("id="+id);
        MentoProcessDto mentoProcessDto = adminService.selectProcess(id);
        MentoDto mentoDto=mentoService.selectmento(id);
        MemberDto memberDto=memberRepository.findmemberByUserID(id);

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
        if (Objects.equals(status, "deny")) {
            mentoProcessDto.setProcess("2");
            adminService.UpdateMentoProcess(mentoProcessDto);
        }
        if (Objects.equals(status, "delete")) {
            ;
            adminService.DeleteMentoProcess(mentoProcessDto);
        }
        return mentoProcessDto;
    }

    @GetMapping("/report")
    public String ReportManagement(Model model) {

        List<CopyrightReportDto> copyrightReportDtoList = adminService.AllCopyRightList();
        for (CopyrightReportDto dto : copyrightReportDtoList) {
            String reporterName = adminService.getReporterNameByEmail(dto.getReporterEmail());
            String reportedName = adminService.getReportedNameById(dto.getReportedID());
            dto.setReporterName(reporterName);
            dto.setReportedName(reportedName);
        }

        model.addAttribute("copyrightReportDtoList", copyrightReportDtoList);
        return "adm/admin_report";
    }

    @GetMapping("/user")
    public String UserManagement(Model model) {
        //삭제시 cascading 모두 삭제되도록 해야 합니다.

        List<UserAdminDto> userAdminDtoList = adminService.AllUserList();
        List<UserAdminDto> userBanDtoList = adminService.AllBannedUserList();
        model.addAttribute("userAdminDtoList", userAdminDtoList);
        model.addAttribute("userBanDtoList", userBanDtoList);
        return "adm/admin_user";
    }

    @GetMapping("/mentoring")
    public String MentoManagement(Model model) {
        List<MentoringDto> mentoringDtoList = adminService.AllMentoringList();

        model.addAttribute("mentoringDtoList", mentoringDtoList);
        //멘토링 삭제시 주의사항
        return "adm/admin_mentoring";
    }

    @GetMapping("/port")
    public String PortManagement(Model model) {
        List<PortAdminDto> portAdminDtoList = adminService.AllPortList();
        for (PortAdminDto dto : portAdminDtoList) {
            String getUserName = adminService.getReportedNameById(dto.getAuthorID());
            dto.setUserName(getUserName);
        }
        //port와 user 번호 받아와서 유저 이름대로 출력
        //port 그냥 삭제하면 될듯
        model.addAttribute("portAdminDtoList", portAdminDtoList);
        return "adm/admin_port";
    }

    @PutMapping("/put/{ReportID}")
    public String ReportPut(@PathVariable("ReportID") int ReportID) {

        System.out.println(ReportID);
        int ChangeState = adminService.UpdateState(ReportID);
        if (ChangeState > 0) {
            return "redirect:/admin/report";
        } else {
            return "error/505";
        }
    }

    @DeleteMapping("/delete/port/{PortfolioID}")
    public String portfolioPortDelete(@PathVariable("PortfolioID") String PortfolioID) {
        portfolio2.portdelete(Integer.parseInt(PortfolioID));
        return "redirect:/admin/port";
    }

    @DeleteMapping("/delete/port/{ReportID}/{PortfolioID}")
    public String portfolioReportDelete(@PathVariable("ReportID") int ReportID, @PathVariable("PortfolioID") String PortfolioID) {
        adminService.UpdateState(ReportID);
        portfolio2.portdelete(Integer.parseInt(PortfolioID));

        return "redirect:/admin/report";
    }


    @DeleteMapping("/delete/user/{ReportID}/{UserID}")
    public String UserReportDelete(@PathVariable("ReportID") int ReportID, @PathVariable("UserID") int UserID) {
        adminService.UpdateState(ReportID);
        adminService.UserBanbyUserID(UserID);
        return "redirect:/admin/report";
    }


    @DeleteMapping("/delete/user/{UserID}")
    public String UserDelete(@PathVariable("UserID") int UserID) {

        adminService.UserBanbyUserID(UserID);
        return "redirect:/admin/user";
    }

    @DeleteMapping("/delete/mentoring/{MentoringID}")
    public String MentoringDelete(@PathVariable("MentoringID") int MentoringID) {
        adminService.deletementoring(MentoringID);
        return "redirect:/admin/mentoring";
    }

    @PutMapping("/lifting/user/{UserID}")
    public String LiftingUser(@PathVariable("UserID") int UserID) {
        log.info("id="+UserID);
        adminService.UserLiftingbyUserID(UserID);
        return "redirect:/admin/user";
    }
}
