package hello.example.porthub.controller;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;


@Controller
@RequiredArgsConstructor
@RequestMapping("/ports")
public class PortfolioController {

    private final MemberRepository memberRepository;


    @GetMapping("/create")
    public String create() {
        return "portfolio/create";
    }

}