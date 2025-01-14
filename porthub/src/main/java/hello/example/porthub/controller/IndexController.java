package hello.example.porthub.controller;

import hello.example.porthub.config.util.CategoryConstants;
import hello.example.porthub.config.util.CookieUtils;
import hello.example.porthub.config.util.SessionUtils;
import hello.example.porthub.domain.*;
import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.repository.MemberRepository;
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
import java.security.Principal;
import java.util.*;


@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final PortfolioService portfolioService;
    private final MentoService mentoService;
    private final MemberRepository memberRepository;

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

    @GetMapping(value = {"/main","/All"})
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


        int mainPortfolioSize = portfolioService.getPortfolioSize();
        List<MainPortViewDto> mainPortViewDtoList = portfolioService.findAllPorts(page, pageSize, order);


        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) mainPortfolioSize / pageSize);
        int buttonPerPage = 10;
        int currentGroup = (int) Math.ceil((double) page / buttonPerPage);
        int groupStart = (currentGroup - 1) * buttonPerPage + 1;
        int groupEnd = Math.min(currentGroup * buttonPerPage, totalPages);

        Map<Integer, String> reverseRecentPort = getCookie(request);

        model.addAttribute("recentPortfolios", reverseRecentPort);

        model.addAttribute("groupStart", groupStart);
        model.addAttribute("groupEnd", groupEnd);

        model.addAttribute("CategoryNameCheck", 0);
        model.addAttribute("mainPortViewDtoList", mainPortViewDtoList);
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

        int checkNum = CategoryConstants.getCheckNum(CategoryName);


        int mainPortfolioSize = portfolioService.getPortfolioSize();

        List<MainPortViewDto> mainPortViewDtoList = portfolioService.findCategoryPorts(page, pageSize, order, checkNum);

        int totalPages = (int) Math.ceil((double) mainPortfolioSize / pageSize);
        int buttonPerPage = 10;
        int currentGroup = (int) Math.ceil((double) page / buttonPerPage);
        int groupStart = (currentGroup - 1) * buttonPerPage + 1;
        int groupEnd = Math.min(currentGroup * buttonPerPage, totalPages);

        Map<Integer, String> reverseRecentPort = getCookie(request);

        model.addAttribute("recentPortfolios", reverseRecentPort);
        model.addAttribute("groupStart", groupStart);
        model.addAttribute("groupEnd", groupEnd);
        model.addAttribute("CategoryNameCheck", checkNum);
        model.addAttribute("Category", categoryDtoList);
        model.addAttribute("CategoryName", CategoryName);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("mainPortViewDtoList", mainPortViewDtoList);
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

        int checkNum = CategoryConstants.getCheckNum(CategoryName);

        int mainPortfolioSize = portfolioService.getPortfolioSize();

        List<MainPortViewDto> mainPortViewDtoList = portfolioService.findAllSearchPorts(page, pageSize, order, SearchQuery, checkNum);

        int totalPages = (int) Math.ceil((double) mainPortfolioSize / pageSize);
        int buttonPerPage = 10;
        int currentGroup = (int) Math.ceil((double) page / buttonPerPage);
        int groupStart = (currentGroup - 1) * buttonPerPage + 1;
        int groupEnd = Math.min(currentGroup * buttonPerPage, totalPages);

        model.addAttribute("mainPortViewDtoList", mainPortViewDtoList);
        model.addAttribute("groupStart", groupStart);
        model.addAttribute("groupEnd", groupEnd);
        model.addAttribute("CategoryNameCheck", checkNum);
        model.addAttribute("Category", categoryDtoList);
        model.addAttribute("CategoryName", CategoryName);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("SearchQuery", SearchQuery);

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
    public String Mento(Model model, Principal principal) {
        if(principal!=null){
            MemberDto member = memberRepository.findByEmail(principal.getName());
            model.addAttribute("member",member);
        }

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
    public String views_all(Model model, HttpServletRequest request) {

        String userEmail = SessionUtils.getCurrentUsername();

        if (userEmail == null) {
            return "redirect:/";
        }

        List<Integer> likeIDs = portfolioService.findLikesByEmail(userEmail);

        List<Integer> historyIDs = new ArrayList<>();
        Map<Integer, String> reverseRecentPort = getCookie(request);
        historyIDs.addAll(reverseRecentPort.keySet());

        boolean hasLikes = !likeIDs.isEmpty();
        boolean hasHistory = !historyIDs.isEmpty();


        if (hasLikes) {
            model.addAttribute("LikesPortViewDtoList", portfolioService.findSelectListPorts(likeIDs));
        } else {
            model.addAttribute("LikesPortViewDtoList", null);
        }
        if (hasHistory) {
            model.addAttribute("HistoryPortViewDtoList", portfolioService.findSelectListPorts(historyIDs));
        } else {
            model.addAttribute("HistoryPortViewDtoList", null);
        }


        model.addAttribute("hasHistory", hasHistory);
        model.addAttribute("hasLikes", hasLikes);

        return "user/view";
    }

    @GetMapping(value = {"/", "/about"})
    public String about() {
        return "user/about";
    }

    @GetMapping(value = {"/register"})
    public String register() {
        return "register/register";
    }


    @PostMapping("/follow/{followingID}")
    public String portsFollowInsert(@PathVariable("followingID") int followingID, HttpServletRequest request) {
        String CurrentUseremail = SessionUtils.getCurrentUsername();
        portfolioService.following(followingID, CurrentUseremail);

        String referer = request.getHeader("Referer"); // 이전 페이지의 URL을 가져옴
        return "redirect:" + referer; // 이전 페이지로 리디렉션
    }

    @PostMapping("/unfollow/{followingID}")
    public String portsFollowDelete(@PathVariable("followingID") int followingID, HttpServletRequest request) {
        String CurrentUseremail = SessionUtils.getCurrentUsername();
        portfolioService.unfollow(followingID, CurrentUseremail);

        String referer = request.getHeader("Referer"); // 이전 페이지의 URL을 가져옴
        return "redirect:" + referer; // 이전 페이지로 리디렉션
    }

    @GetMapping("/session-expired")
    public String sessionExpired() {
        return "SessionHandler/session_Expired";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "SessionHandler/logout-success";
    }

}

