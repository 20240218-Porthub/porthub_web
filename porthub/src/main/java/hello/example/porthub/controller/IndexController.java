package hello.example.porthub.controller;

import hello.example.porthub.domain.CategoryDto;
import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.domain.MentoViewDto;
import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.domain.MentoringDto;
import hello.example.porthub.service.MentoService;
import hello.example.porthub.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;


@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final PortfolioService portfolioService;
    private final MentoService mentoService;


    @GetMapping(value = {"/", "/main","/{CategoryName}"})
    public String index(@PathVariable("CategoryName") String CategoryName, Model model) {
        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();
        List<MainPortViewDto> mainPortViewDtoList = portfolioService.findAllPorts();
        model.addAttribute("mainPortViewDtoList", mainPortViewDtoList);
        System.out.println(mainPortViewDtoList);
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
    public String Mento(Model model) {
        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();
        List<MentoViewDto> mentorings=mentoService.allmentoring();
        model.addAttribute("Category", categoryDtoList);
        model.addAttribute("mentorings",mentorings);
        return "mentoring/mentoring";
    }

    @GetMapping(value = {"/chat"})
    public String chat() {
        return "user/chat";
    }

    @GetMapping(value={"/profile"})
    public String profile() { return "user/profile"; }

    @GetMapping(value = {"/about"})
    public String about() {
        return "user/about";
    }


    @GetMapping(value = {"/register"})
    public String register() {
        return "register/register";
    }


}

