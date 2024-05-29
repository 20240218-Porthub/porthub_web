package hello.example.porthub.controller;

import hello.example.porthub.config.util.CookieUtils;
import hello.example.porthub.config.util.SessionUtils;
import hello.example.porthub.domain.*;
import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.service.MentoService;
import hello.example.porthub.service.PortfolioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final PortfolioService portfolioService;
    private final MentoService mentoService;

    public Map<Integer, String> getCookie(HttpServletRequest request) {
        Map<String, String> recentPortfolios = CookieUtils.getCookieData(request.getCookies(), CookieUtils.COOKIE_NAME);
        Map<Integer, String> reverseRecentPort = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>(recentPortfolios.keySet());

        Collections.reverse(keys);
        for (String key : keys) {
            int intKey = Integer.parseInt(key); // String key를 int로 변환
            reverseRecentPort.put(intKey, recentPortfolios.get(key));
        }
        return reverseRecentPort;
    }

    @GetMapping(value = {"/", "/main","/All"})
    public String index(@RequestParam(value = "order", defaultValue = "NewestOrder") String order,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                        Model model, HttpServletRequest request) {
        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();
        List<PopularDto> popularDtoList = portfolioService.findByPopular();

        if (SessionUtils.isLoggedIn()) {
            model.addAttribute("isLoggedIn", true);
            boolean followCheck = false;
            for (PopularDto dto : popularDtoList) {
                followCheck = portfolioService.checkFollow(dto.getUserID(), SessionUtils.getCurrentUsername());
                if (followCheck) {
                    dto.setFollowCheck(true);
                } else {
                    dto.setFollowCheck(false);
                }
            }
        } else {
            model.addAttribute("isLoggedIn", false);
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


        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) mainPortViewDtoList.size() / pageSize);

        // 페이지 범위 계산
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, mainPortViewDtoList.size());
        List<MainPortViewDto> pagedMainPortViewDtoList = mainPortViewDtoList.subList(fromIndex, toIndex);

        Map<Integer, String> reverseRecentPort = getCookie(request);

        model.addAttribute("recentPortfolios", reverseRecentPort);



        model.addAttribute("CategoryNameCheck", 0);
        model.addAttribute("mainPortViewDtoList", pagedMainPortViewDtoList);
        model.addAttribute("Category", categoryDtoList);
        model.addAttribute("CategoryName", "All");
        model.addAttribute("selectedOrder", order);
        model.addAttribute("currentPage", page); // 현재 페이지 추가
        model.addAttribute("pageSize", pageSize); // 페이지 사이즈 추가
        model.addAttribute("totalPages", totalPages); // 전체 페이지 수 추가
        model.addAttribute("checkSearchNum", 1);
        model.addAttribute("PopularViewDtoList", popularDtoList);

        return "portfolio/main";
    }

    @GetMapping({"/{CategoryName}"})
    public String CategoryPort(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                               @PathVariable("CategoryName") String CategoryName, @RequestParam(value = "order",
            defaultValue = "NewestOrder") String order, Model model, HttpServletRequest request) {

        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();
        List<PopularDto> popularDtoList = portfolioService.findByPopular();
        if (SessionUtils.isLoggedIn()) {
            model.addAttribute("isLoggedIn", true);
            boolean followCheck = false;
            for (PopularDto dto : popularDtoList) {
                followCheck = portfolioService.checkFollow(dto.getUserID(), SessionUtils.getCurrentUsername());
                if (followCheck) {
                    dto.setFollowCheck(true);
                } else {
                    dto.setFollowCheck(false);
                }
            }
        } else {
            model.addAttribute("isLoggedIn", false);
        }

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

        Map<Integer, String> reverseRecentPort = getCookie(request);

        model.addAttribute("recentPortfolios", reverseRecentPort);

        model.addAttribute("CategoryNameCheck", checkNum);
        model.addAttribute("Category", categoryDtoList);
        model.addAttribute("CategoryName", CategoryName);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("mainPortViewDtoList", pagedMainPortViewDtoList);
        model.addAttribute("currentPage", page); // 현재 페이지 추가
        model.addAttribute("pageSize", pageSize); // 페이지 사이즈 추가
        model.addAttribute("totalPages", totalPages); // 전체 페이지 수 추가
        model.addAttribute("checkSearchNum", 1);
        model.addAttribute("PopularViewDtoList", popularDtoList);
        return "portfolio/main";
    }


    @GetMapping(value = {"{CategoryName}/search", "/main/search"})
    public String searchPortfolios(@PathVariable(value = "CategoryName", required = false) String CategoryName,
                                   @RequestParam(value = "SearchQuery", defaultValue = "") String SearchQuery,
                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                   @RequestParam(value = "order", defaultValue = "NewestOrder") String order,
                                   Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        List<CategoryDto> categoryDtoList = portfolioService.findByCategory();
        List<PopularDto> popularDtoList = portfolioService.findByPopular();
        if (SessionUtils.isLoggedIn()) {
            model.addAttribute("isLoggedIn", true);
            boolean followCheck = false;
            for (PopularDto dto : popularDtoList) {
                followCheck = portfolioService.checkFollow(dto.getUserID(), SessionUtils.getCurrentUsername());
                if (followCheck) {
                    dto.setFollowCheck(true);
                } else {
                    dto.setFollowCheck(false);
                }
            }
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        if (CategoryName==null) {
            CategoryName = "main";
        }
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
                mainPortViewDtoList = portfolioService.findAllSearchPortsOrderByPopularity(SearchQuery);
                break;
            case "ViewsOrder":
                mainPortViewDtoList = portfolioService.findAllSearchPortsOrderByViews(SearchQuery);
                break;
            case "OldestOrder":
                mainPortViewDtoList = portfolioService.findAllSearchPortsOrderByOldest(SearchQuery);
                break;
            case "NewestOrder":
            default:
                mainPortViewDtoList = portfolioService.findAllSearchPorts(SearchQuery);
                break;
        }

        if (!CategoryName.equals("main")) {
            List<MainPortViewDto> selectedPortViewDtoList = portfolioService.findPortsByCategory(mainPortViewDtoList, checkNum);
            mainPortViewDtoList = selectedPortViewDtoList;

            int totalPages = (int) Math.ceil((double) mainPortViewDtoList.size() / pageSize);
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, mainPortViewDtoList.size());

            List<MainPortViewDto> pagedMainPortViewDtoList = mainPortViewDtoList.subList(fromIndex, toIndex);
            model.addAttribute("mainPortViewDtoList", pagedMainPortViewDtoList);
            model.addAttribute("CategoryNameCheck", checkNum);
            model.addAttribute("Category", categoryDtoList);
            model.addAttribute("CategoryName", CategoryName);
            model.addAttribute("selectedOrder", order);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("SearchQuery", SearchQuery);
        } else {

            int totalPages = (int) Math.ceil((double) mainPortViewDtoList.size() / pageSize);
            // 페이지 범위 계산
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, mainPortViewDtoList.size());
            List<MainPortViewDto> pagedMainPortViewDtoList = mainPortViewDtoList.subList(fromIndex, toIndex);

            model.addAttribute("CategoryNameCheck", 0);
            model.addAttribute("mainPortViewDtoList", pagedMainPortViewDtoList);
            model.addAttribute("Category", categoryDtoList);
            model.addAttribute("CategoryName", CategoryName);
            model.addAttribute("CategoryNameCheck", checkNum);
            model.addAttribute("selectedOrder", order);
            model.addAttribute("currentPage", page); // 현재 페이지 추가
            model.addAttribute("pageSize", pageSize); // 페이지 사이즈 추가
            model.addAttribute("totalPages", totalPages); // 전체 페이지 수 추가
            model.addAttribute("SearchQuery", SearchQuery);
        }

        Map<Integer, String> reverseRecentPort = getCookie(request);

        model.addAttribute("recentPortfolios", reverseRecentPort);
        int checkSearchNum = 0;
        String encodedSearchQuery = URLEncoder.encode(SearchQuery, "UTF-8");

        model.addAttribute("encodedSearchQuery", encodedSearchQuery);
        model.addAttribute("checkSearchNum", checkSearchNum);
        model.addAttribute("PopularViewDtoList", popularDtoList);

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

    @GetMapping(value={"/view"})
    public String views_all() { return "user/view"; }

    @GetMapping(value = {"/about"})
    public String about() {
        return "user/about";
    }

    @GetMapping(value = {"/register"})
    public String register() {
        return "register/register";
    }


    @PostMapping("/follow/{followingID}")
    public String portsFollowInsert(@PathVariable("followingID") int followingID) {
        String CurrentUseremail = SessionUtils.getCurrentUsername();
        portfolioService.following(followingID, CurrentUseremail);

        return "redirect:/main";
    }

    @PostMapping("/unfollow/{followingID}")
    public String portsFollowDelete(@PathVariable("followingID") int followingID) {
        String CurrentUseremail = SessionUtils.getCurrentUsername();
        portfolioService.unfollow(followingID, CurrentUseremail);
        return "redirect:/main";
    }

}

