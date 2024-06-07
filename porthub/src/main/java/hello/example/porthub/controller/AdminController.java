package hello.example.porthub.controller;


import hello.example.porthub.domain.*;
import hello.example.porthub.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        System.out.println(copyrightReportDtoList);
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
    public String UserManagement() {
        //삭제시 cascading 모두 삭제되도록 해야 합니다.
//        List<MemberDto> memberDtoList = adminService.AllUserList();
        return "adm/admin_user";
    }
    @GetMapping("/mentoring")
    public String MentoManagement() {

        //멘토링 삭제시 주의사항
        return "adm/admin_mentoring";
    }

    @GetMapping("/port")
    public String PortManagement() {
//        List<PortfolioDto> portfolioDto = adminService.AllPortList();
        //port와 user 번호 받아와서 유저 이름대로 출력
        //port 그냥 삭제하면 될듯
        return "adm/admin_port";
    }
}
