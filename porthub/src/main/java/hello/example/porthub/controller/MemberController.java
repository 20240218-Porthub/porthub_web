package hello.example.porthub.controller;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class MemberController {
    //dependency injection
    private final MemberService memberService;

    @GetMapping("/save")
    public String saveForm() {
        return "register/register";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDto memberDto) {
        int saveResult = memberService.save(memberDto);
//        예제입니다.
        if (saveResult > 0) {
            return "register/login"; //가입 성공
        } else {
            return "register/register"; //가입 실패
        }

    }

    @PostMapping("/email-check")
    public @ResponseBody String emailCheck(@RequestParam("Email") String Email) {
        System.out.println("Email" + Email);
        String checkResult = memberService.emailCheck(Email);
        return checkResult;
    }
}
