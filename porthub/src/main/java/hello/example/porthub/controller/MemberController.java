package hello.example.porthub.controller;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
