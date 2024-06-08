package hello.example.porthub.controller;


import hello.example.porthub.config.util.SessionUtils;
import hello.example.porthub.domain.*;
import hello.example.porthub.domain.AdminDto.PortAdminDto;
import hello.example.porthub.domain.AdminDto.UserAdminDto;
import hello.example.porthub.service.AdminService;
import hello.example.porthub.service.PortfolioService;
import jakarta.servlet.http.HttpSession;
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
    private final PortfolioService portfolio2;

    @GetMapping("")
    public String admin() {
        return "adm/admin";
    }

    @GetMapping("/mento")
    public String AdminMento(Model model) {
        List<MentoProcessDto> requestmento = adminService.AllRequestMentoProcess();
        List<MentoProcessDto> allmento = adminService.AllMento();

        log.info("request=" + requestmento + ", allmento=" + allmento);

        model.addAttribute("requestmentos", requestmento);
        model.addAttribute("allmento", allmento);

        return "adm/admin_mento";
    }

    @PostMapping("/mento/{status}/{id}")
    @ResponseBody
    public MentoProcessDto AcceptMento(@PathVariable String status, @PathVariable int id) {
        MentoProcessDto mentoProcessDto = new MentoProcessDto();
        mentoProcessDto.setProcessID(id);
        if (Objects.equals(status, "accept")) {
            mentoProcessDto.setProcess("1");
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

        model.addAttribute("userAdminDtoList", userAdminDtoList);
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


    @DeleteMapping("/delete/user/{UserID}")
    public String UserDelete(@PathVariable("UserID") String UserID) {
//        삭제시 주의해야함!!!!
        return "redirect:/";
    }

    @DeleteMapping("/delete/mentoring/{MentoID}")
    public String MentoringDelete(@PathVariable("MentoID") String MentoID) {
//        일단 냅두기

        return "redirect:/";
    }

}
