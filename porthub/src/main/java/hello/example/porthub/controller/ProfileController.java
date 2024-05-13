package hello.example.porthub.controller;

import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.ProfileDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.ProfileRepository;
import hello.example.porthub.service.ProfileService;
import hello.example.porthub.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final S3Service s3Service;

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
        List<MainPortViewDto> mainPortView=profileService.findPortByUserID(userid);
        modelMap.addAttribute("mainPortView", mainPortView);
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
            modelMap.addAttribute("member", member);
            modelMap.addAttribute("UserMeta",UserMeta);
            return "user/profileedit";
        }
    }

    @PostMapping("/{name}/edit/save")
    public String saveProfile(@PathVariable("name") String name, @ModelAttribute ProfileDto profiledto) throws IOException {
        MemberDto memberDto=memberRepository.findByUserName(name);
        profiledto.setUserID(memberDto.getUserID());
        memberDto.setUserID(memberDto.getUserID());

        if(!profiledto.getProfileimage().isEmpty()) {
            memberDto.setProfileImage(s3Service.uploadFiles(profiledto.getProfileimage()));
        }
        if(!profiledto.getBackimg().isEmpty()) {
            memberDto.setBackImage(s3Service.uploadFiles(profiledto.getBackimg()));
        }

        profileRepository.metasave(profiledto);
        memberRepository.imagesave(memberDto);
        return "redirect:/profile/"+name;
    }
}
