package hello.example.porthub.controller;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.ProfileDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;

    @GetMapping("/index")
    public String getUsername(Principal principal) {
        String loginId = principal.getName();
        MemberDto member = memberRepository.findByEmail(loginId);
        return "redirect:/profile/"+member.getUserName();
    }

    @GetMapping("/{name}")
    public String memberInfo(@PathVariable("name") String name, ModelMap modelMap){
        MemberDto member = memberRepository.findByUserName(name);
        int userid=member.getUserID();
        ProfileDto UserMeta=profileRepository.findByUserID(userid);
        log.info("meta="+UserMeta);
        modelMap.addAttribute("member", member);
        modelMap.addAttribute("UserMeta",UserMeta);

        return "user/profile";
    }

    @GetMapping("/{name}/edit")
    public String editProfile(@PathVariable("name") String name,Principal principal, ModelMap modelMap){
        if(principal== null){return "redirect:/profile/"+name;}
        String loginId = principal.getName();
        MemberDto member = memberRepository.findByEmail(loginId);
        if(!Objects.equals(name, member.getUserName())){
            return "redirect:/profile/"+name;
        }
        else{
            int userid=member.getUserID();
            ProfileDto UserMeta=profileRepository.findByUserID(userid);
            log.info("member="+member);
            log.info("meta="+UserMeta);
            modelMap.addAttribute("member", member);
            modelMap.addAttribute("UserMeta",UserMeta);

            return "user/profileedit";
        }
    }

    @PostMapping("/{name}/edit/save")
    public String saveProfile(@PathVariable("name") String name, @ModelAttribute ProfileDto profiledto){
        profiledto.setUserID(memberRepository.findByUserName(name).getUserID());
        log.info("formdata="+profiledto);
        profileRepository.metasave(profiledto);
        return "redirect:/profile/"+name;
    }
}
