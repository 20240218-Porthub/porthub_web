package hello.example.porthub.controller;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@RequestMapping("/ports")
public class PortfolioController {

    private final MemberRepository memberRepository;
    @GetMapping("/{userName}")
    public String create(Principal principal, ModelMap modelMap) {
        String loginId = principal.getName();
        MemberDto member = memberRepository.findByEmail(loginId);
        return "redirect:/portfolio/create/" + member.getUserName();
    }

    @GetMapping("/portfolio/create/{userName}")
    public String showPortfolioCreateForm(@PathVariable String userName, ModelMap modelMap) {
        // 여기에 포트폴리오 생성 폼을 보여주는 로직을 구현하세요.
        // 예를 들어, 모델에 필요한 데이터를 추가하고, 포트폴리오 생성 페이지의 뷰 이름을 반환합니다.
        return "portfolio/create";
    }

}