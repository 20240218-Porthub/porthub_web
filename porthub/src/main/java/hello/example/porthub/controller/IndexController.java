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
import org.springframework.web.bind.annotation.ModelAttribute;
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


    @GetMapping(value = {"/", "/main"})
    public String index(Model model) {
        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();
        List<MainPortViewDto> mainPortViewDtoList = portfolioService.findAllPorts();
        model.addAttribute("CategoryNameCheck", 0);
        model.addAttribute("mainPortViewDtoList", mainPortViewDtoList);
        model.addAttribute("Category", categoryDtoList);
        return "portfolio/main";
    }
    @GetMapping({"/{CategoryName}"})
    public String CategoryPort(@PathVariable("CategoryName") String CategoryName, Model model) {
        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();
        List<MainPortViewDto> mainPortViewDtoList = portfolioService.findAllPorts();
        int checkNum = 0;
        if (CategoryName.equals("Development")) {
            checkNum = 2;
        } else if (CategoryName.equals("Music")) {
            checkNum = 3;
        } else if (CategoryName.equals("Design")) {
            checkNum = 4;
        } else if (CategoryName.equals("Editing")) {
            checkNum = 5;
        } else if (CategoryName.equals("Film")) {
            checkNum = 6;
        } else if (CategoryName.equals("Marketing")) {
            checkNum = 7;
        } else if (CategoryName.equals("Other")) {
            checkNum = 8;
        }
        model.addAttribute("CategoryNameCheck", checkNum);
        model.addAttribute("mainPortViewDtoList", mainPortViewDtoList);
        System.out.println(mainPortViewDtoList);
        model.addAttribute("Category", categoryDtoList);
        model.addAttribute("CategoryName", CategoryName);
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
        return "redirect:/user/chat";
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

