package hello.example.porthub.controller;

import hello.example.porthub.domain.CategoryDto;
import hello.example.porthub.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final PortfolioService portfolioService;


    @GetMapping(value = {"/", "/main"})
    public String index(Model model) {
        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();
        model.addAttribute("Category", categoryDtoList);
        return "portfolio/main";
    }

    @GetMapping(value = {"/login"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "register/login";
    }

    @GetMapping("/mentoring")
    public String Mento() {
        return "mentoring/mentoring";
    }

    @GetMapping(value = {"/login2"})
    public String login2() {
        return "register/login2";
    }

    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }

    @GetMapping(value = {"/register"})
    public String register() {
        return "register/register";
    }


}

