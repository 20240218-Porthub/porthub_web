package hello.example.porthub.controller;


import hello.example.porthub.domain.MentoProcessDto;
import hello.example.porthub.service.AdminService;
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
    @GetMapping("")
    public String admin() {
        return "adm/admin";
    }

    @GetMapping("/mento")
    public String AdminMento(Model model){
        List<MentoProcessDto> requestmento = adminService.AllRequestMentoProcess();
        List<MentoProcessDto> allmento = adminService.AllMento();

        log.info("request="+requestmento+", allmento="+allmento);

        model.addAttribute("requestmentos",requestmento);
        model.addAttribute("allmento",allmento);

        return "adm/admin_mento";
    }

    @PostMapping("/mento/{status}/{id}")
    @ResponseBody
    public MentoProcessDto AcceptMento(@PathVariable String status, @PathVariable int id){
        MentoProcessDto mentoProcessDto = new MentoProcessDto();
        mentoProcessDto.setProcessID(id);
        if(Objects.equals(status, "accept")){
            mentoProcessDto.setProcess("1");
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
