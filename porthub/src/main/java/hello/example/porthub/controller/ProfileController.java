package hello.example.porthub.controller;

import hello.example.porthub.config.util.SessionUtils;
import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.PopularDto;
import hello.example.porthub.domain.ProfileDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.ProfileRepository;
import hello.example.porthub.service.PortfolioService;
import hello.example.porthub.service.ProfileService;
import hello.example.porthub.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final PortfolioService portfolioService;
    private final S3Service s3Service;

    @GetMapping("/index")
    public String getUsername(Principal principal) {
        String loginId = principal.getName();
        MemberDto member = memberRepository.findByEmail(loginId);
        return "redirect:/profile/"+member.getUserName();
    }

    @GetMapping("/{name}")
    public String memberInfo(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @PathVariable("name") String name, ModelMap modelMap, Model model) {

        Map<String,String> map= new HashMap<String, String>();
        MemberDto member = memberRepository.findByUserName(name);
        int userid=member.getUserID();
        ProfileDto UserMeta=profileRepository.findByUserID(userid);



        List<Integer> getfolloweruserList = profileRepository.getUserFollowerListbyID(userid);

        List<Integer> getfollowinguserList = profileRepository.getUserFollowingListbyID(userid);

        List<PopularDto> follwerDataList = null, follwingDataList = null;

        if (!getfolloweruserList.isEmpty()) {
            follwerDataList = portfolioService.getFollowList(getfolloweruserList);
        }
        if (!getfollowinguserList.isEmpty()) {
            follwingDataList = portfolioService.getFollowList(getfollowinguserList);
        }


        if (SessionUtils.isLoggedIn()) {
            model.addAttribute("isLoggedIn", true);
            boolean followCheck = portfolioService.checkFollow(userid, SessionUtils.getCurrentUsername());
            model.addAttribute("followCheck", followCheck);

            if (follwerDataList != null) {
                for (PopularDto dto : follwerDataList) {
                    dto.setFollowCheck(portfolioService.checkFollow(dto.getUserID(), SessionUtils.getCurrentUsername()));
                }
            }

            if (follwingDataList != null) {
                for (PopularDto dto : follwingDataList) {
                    dto.setFollowCheck(portfolioService.checkFollow(dto.getUserID(), SessionUtils.getCurrentUsername()));
                }
            }
            //로그인 되어있는 경우 사용자 아이디
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        int totalPortfolios = profileService.countPortfoliosByUserID(userid);

        int totalPages = (int) Math.ceil((double) totalPortfolios / pageSize);
        List<MainPortViewDto> mainPortView = profileService.findPortByUserID(userid, page, pageSize);
        int buttonPerPage = 10;
        int currentGroup = (int) Math.ceil((double) page / buttonPerPage);
        int groupStart = (currentGroup - 1) * buttonPerPage + 1;
        int groupEnd = Math.min(currentGroup * buttonPerPage, totalPages);


        map.put("follower", String.valueOf(getfolloweruserList.size()));
        map.put("following",String.valueOf(getfollowinguserList.size()));

        model.addAttribute("groupStart", groupStart);
        model.addAttribute("groupEnd", groupEnd);

        model.addAttribute("follwerDataList", follwerDataList);
        model.addAttribute("follwingDataList", follwingDataList);

        modelMap.addAttribute("pageSize", pageSize);
        modelMap.addAttribute("totalPages", totalPages);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("follows",map);
        modelMap.addAttribute("mainPortView", mainPortView);
        modelMap.addAttribute("member", member);
        modelMap.addAttribute("userId", userid);
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

    @PostMapping("/follow/{UserName}")
    public String portsFollowInsert(@PathVariable("UserName") String UserName) {

        int authorID = portfolioService.findUserIDbyUserName(UserName);
        String CurrentUseremail = SessionUtils.getCurrentUsername();
        portfolioService.following(authorID, CurrentUseremail);

        return "redirect:/profile/" + UserName;
    }

    @PostMapping("/unfollow/{UserName}")
    public String portsFollowDelete(@PathVariable("UserName") String UserName) {

        int authorID = portfolioService.findUserIDbyUserName(UserName);
        String CurrentUseremail = SessionUtils.getCurrentUsername();
        portfolioService.unfollow(authorID, CurrentUseremail);

        return "redirect:/profile/" + UserName;
    }
}
