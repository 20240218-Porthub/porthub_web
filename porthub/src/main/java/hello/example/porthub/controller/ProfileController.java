package hello.example.porthub.controller;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.ProfileDto;
import hello.example.porthub.repository.ProfileRepository;
import hello.example.porthub.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final MemberService memberService;

    @GetMapping("/index")
    public String getUsername(Principal principal, ModelMap modelMap) {
        String loginId = principal.getName();
        MemberDto member = memberService.getMemberByEmail(loginId);
        return "redirect:/profile/"+member.getUserName();
    }

    @GetMapping("/{name}")
    public String memberInfo(@PathVariable("name") String name, ModelMap modelMap){
        MemberDto member = memberService.getMemberByUserName(name);
        int userid=member.getUserID();
        ProfileDto UserMeta=memberService.getUsermetaByUserID(userid);
        log.info("meta="+UserMeta);
        modelMap.addAttribute("member", member);
        modelMap.addAttribute("UserMeta",UserMeta);

        return "user/profile";
    }
}
