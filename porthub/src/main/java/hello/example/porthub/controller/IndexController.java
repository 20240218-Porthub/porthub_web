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


    @GetMapping(value = {"/", "/main","/All"})
    public String index(@RequestParam(value = "order", defaultValue = "NewestOrder") String order,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                        Model model) {
        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();
        List<MainPortViewDto> mainPortViewDtoList;
        switch (order) {
            case "PopularityOrder":
                mainPortViewDtoList = portfolioService.findAllPortsOrderByPopularity();
                break;
            case "ViewsOrder":
                mainPortViewDtoList = portfolioService.findAllPortsOrderByViews();
                break;
            case "OldestOrder":
                mainPortViewDtoList = portfolioService.findAllPortsOrderByOldest();
                break;
            case "NewestOrder":
            default:
                mainPortViewDtoList = portfolioService.findAllPorts();
                break;
        }

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) mainPortViewDtoList.size() / pageSize);

        // 페이지 범위 계산
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, mainPortViewDtoList.size());
        List<MainPortViewDto> pagedMainPortViewDtoList = mainPortViewDtoList.subList(fromIndex, toIndex);


        model.addAttribute("CategoryNameCheck", 0);
        model.addAttribute("mainPortViewDtoList", pagedMainPortViewDtoList);
        model.addAttribute("Category", categoryDtoList);
        model.addAttribute("CategoryName", "All");
        model.addAttribute("selectedOrder", order);
        model.addAttribute("currentPage", page); // 현재 페이지 추가
        model.addAttribute("pageSize", pageSize); // 페이지 사이즈 추가
        model.addAttribute("totalPages", totalPages); // 전체 페이지 수 추가

        return "portfolio/main";
    }

    @GetMapping({"/{CategoryName}"})
    public String CategoryPort(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                               @PathVariable("CategoryName") String CategoryName, @RequestParam(value = "order",
            defaultValue = "NewestOrder") String order, Model model) {

        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();

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

        List<MainPortViewDto> mainPortViewDtoList;
        switch (order) {
            case "PopularityOrder":
                mainPortViewDtoList = portfolioService.findAllPortsOrderByPopularity();
                break;
            case "ViewsOrder":
                mainPortViewDtoList = portfolioService.findAllPortsOrderByViews();
                break;
            case "OldestOrder":
                mainPortViewDtoList = portfolioService.findAllPortsOrderByOldest();
                break;
            case "NewestOrder":
            default:
                mainPortViewDtoList = portfolioService.findAllPorts();
                break;
        }
        List<MainPortViewDto> selectedPortViewDtoList = portfolioService.findPortsByCategory(mainPortViewDtoList, checkNum);
        mainPortViewDtoList = selectedPortViewDtoList;


        int totalPages = (int) Math.ceil((double) mainPortViewDtoList.size() / pageSize);

        // 페이지 범위 계산
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, mainPortViewDtoList.size());
        List<MainPortViewDto> pagedMainPortViewDtoList = mainPortViewDtoList.subList(fromIndex, toIndex);


        model.addAttribute("CategoryNameCheck", checkNum);
        System.out.println(mainPortViewDtoList);
        model.addAttribute("Category", categoryDtoList);
        model.addAttribute("CategoryName", CategoryName);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("mainPortViewDtoList", pagedMainPortViewDtoList);
        model.addAttribute("currentPage", page); // 현재 페이지 추가
        model.addAttribute("pageSize", pageSize); // 페이지 사이즈 추가
        model.addAttribute("totalPages", totalPages); // 전체 페이지 수 추가
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

