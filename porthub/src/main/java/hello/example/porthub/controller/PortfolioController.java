package hello.example.porthub.controller;
import hello.example.porthub.config.util.CookieUtils;
import hello.example.porthub.config.util.SessionUtils;
import hello.example.porthub.domain.*;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.service.PortfolioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/ports")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final MemberRepository memberRepository;

    private static final int MAX_ENTRIES = 5;
    private static final String COOKIE_NAME = "portfolioCookie";

    @PostMapping("/uploads")
    public String uploadPortfolio(@ModelAttribute PortfolioDto portfolioDto) throws IOException {

        // form data 전송시 List 형태로 담아두었음. getContent(), getFile()시 여러개의 파일이 담겨있음
        // 이를 순서대로 db에 저장하는 로직을 구현해야 함.

        int uploadResult = portfolioService.upload(portfolioDto);

        if (uploadResult > 0) {
            return "redirect:/main";
        } else {
            return "redirect:/ports/create";
        }
    }

    @GetMapping("/create")
    public String create() {
        return "portfolio/create";
    }


    @GetMapping("/views/{PortfolioID}")
    public String portfolioDetail(@PathVariable(name = "PortfolioID") int PortfolioID, Model model, HttpSession session,
                                  HttpServletRequest request, HttpServletResponse response) {

        String viewsPort = (String) session.getAttribute("viewsPort");

        if (viewsPort == null || viewsPort.isEmpty()) {
            // 새로운 포트폴리오를 조회한 경우이므로, 해당 포트폴리오의 ID를 viewsPort에 추가하고 조회수를 증가시킵니다.
            viewsPort = String.valueOf(PortfolioID);
            session.setAttribute("viewsPort", viewsPort); // 세션에 업데이트된 viewsPort를 저장합니다.
            portfolioService.updateViewsCount(PortfolioID); // 조회수를 증가시킵니다.
        } else {
            // 이미 조회한 포트폴리오의 ID가 포함되어 있는지 확인합니다.
            if (!viewsPort.contains(String.valueOf(PortfolioID))) {
                // 그렇지 않은 경우, 현재 조회한 포트폴리오의 ID를 viewsPort에 추가하고 조회수를 증가시킵니다.
                viewsPort += "," + PortfolioID;
                session.setAttribute("viewsPort", viewsPort); // 세션에 업데이트된 viewsPort를 저장합니다.
                portfolioService.updateViewsCount(PortfolioID); // 조회수를 증가시킵니다.
            }
            // 이미 조회한 포트폴리오의 ID가 포함되어 있다면, 중복 조회이므로 추가적인 처리를 하지 않습니다.
        }

        // 포트폴리오 조회수 증가 로직을 수행합니다.

        // 세션에 업데이트된 viewedPortfolios를 다시 저장합니다.
        session.setAttribute("viewedPortfolios", viewsPort);


        PortViewDto portViewDto = portfolioService.findportview(PortfolioID);

        if (portViewDto == null) {
            return "error/noUrl";
        }

        model.addAttribute("PortViewDtoList", portViewDto);
        List<ImagesDto> fileDtoList = portfolioService.findportFiles(PortfolioID);
        model.addAttribute("FileViewDtoList", fileDtoList);
        List<PortViewDto> portuserList = portfolioService.finduserport(PortfolioID);
        model.addAttribute("portuserList", portuserList);

        if (portViewDto.getThumbnail_url()==null) {
            portViewDto.setThumbnail_url("/images/None_Thumbnail.jpeg");
        }
        CookieUtils.addPortfolioData(request.getCookies(), response, CookieUtils.COOKIE_NAME, String.valueOf(PortfolioID), portViewDto.getThumbnail_url());

        if (SessionUtils.isLoggedIn()) {
            model.addAttribute("isLoggedIn", true);
            boolean followCheck = portfolioService.checkFollow(portViewDto.getAuthorID(), SessionUtils.getCurrentUsername());
            model.addAttribute("followCheck", followCheck);
            //로그인 되어있는 경우 사용자 아이디
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        if (portViewDto.getEmail() == SessionUtils.getCurrentUsername()) {
            model.addAttribute("myPort", true);
        } else {

            model.addAttribute("myPort", false);
        }

        boolean heartCheck = portfolioService.checkHeart(PortfolioID, SessionUtils.getCurrentUsername());
        model.addAttribute("heartCheck", heartCheck);

        //session에 필요한 저보 저장
        session.setAttribute("heartCheck", heartCheck);
        session.setAttribute("portfolioID", portViewDto.getPortfolioID());
        session.setAttribute("authorID", portViewDto.getAuthorID());
        session.setAttribute("getEmail",portViewDto.getEmail());

        return "portfolio/portview"; // 포트폴리오 상세 페이지 템플릿 이름을 반환합니다.
    }

    @PostMapping("/views/report")
    public String postReport(@RequestBody CopyrightReportDto copyrightReportDto, HttpSession session) {

        int portfolioID = (int) session.getAttribute("portfolioID");
        int authorID = (int) session.getAttribute("authorID");
        String email = SessionUtils.getCurrentUsername();

        // 위에서 이미 요청 본문의 JSON 데이터를 CopyrightReportDto 객체로 변환했으므로
        // 따로 contents를 추출할 필요가 없습니다.

        copyrightReportDto.setPortfolioID(portfolioID);
        copyrightReportDto.setReportedID(authorID);
        copyrightReportDto.setReporterEmail(email);


        portfolioService.postReportdata(copyrightReportDto);

        return "redirect:/ports/views/" + portfolioID;
    }

    @GetMapping("/views/like")
    public String postLikes(PortLikeDto portLikeDto, HttpSession session) {

        int portfolioID = (int) session.getAttribute("portfolioID");
        String email = SessionUtils.getCurrentUsername();
        boolean heartCheck = (boolean) session.getAttribute("heartCheck");

        // 위에서 이미 요청 본문의 JSON 데이터를 CopyrightReportDto 객체로 변환했으므로
        // 따로 contents를 추출할 필요가 없습니다.

        portLikeDto.setPortfolioID(portfolioID);
        portLikeDto.setEmail(email);
        if (heartCheck) {
            portLikeDto.setHeart_Check(false);
            portfolioService.portfolioDecreLikes(portfolioID);
        } else {
            portLikeDto.setHeart_Check(true);
            portfolioService.portfolioIncreLikes(portfolioID);
        }
        //db에서 데이터 없을 경우 default = 0으로 insert하고 데이터 존재하는 경우 Check_Heart 역전 시키기
//        설정값대로
        portfolioService.convertLikes(portLikeDto);

        return "redirect:/ports/views/" + portfolioID;
    }

    @PostMapping("/views/follow")
    public String portsFollowInsert(HttpSession session) {
        int portfolioID = (int) session.getAttribute("portfolioID");
        int authorID = (int) session.getAttribute("authorID");
        String CurrentUseremail = SessionUtils.getCurrentUsername();
        portfolioService.following(authorID, CurrentUseremail);

        return "redirect:/ports/views/" + portfolioID;
    }

    @PostMapping("/views/unfollow")
    public String portsFollowDelete(HttpSession session) {
        int portfolioID = (int) session.getAttribute("portfolioID");
        int authorID = (int) session.getAttribute("authorID");
        String CurrentUseremail = SessionUtils.getCurrentUsername();
        portfolioService.unfollow(authorID, CurrentUseremail);

        return "redirect:/ports/views/" + portfolioID;
    }


    @DeleteMapping("/views/delete/{PortfolioID}")
    public String portfolioDelete(@PathVariable("PortfolioID") String PortfolioID, HttpSession session) {
        String getEmail = (String) session.getAttribute("getEmail");
        String CurrentUseremail = SessionUtils.getCurrentUsername();

        if (getEmail != null && getEmail.equals(CurrentUseremail)) {
            portfolioService.portdelete(Integer.parseInt(PortfolioID));
        } else {
            return "redirect:/403";
        }

        return "redirect:/main";
    }

    @GetMapping("/views/put/{userName}/{PortfolioID}")
    public String portfolioput(@PathVariable("PortfolioID") String PortfolioID,@PathVariable("userName") String userName, Principal principal, ModelMap model) {

        if (principal == null) {
            return "redirect:/";
        }

        int portID = Integer.parseInt(PortfolioID);
        MemberDto member = memberRepository.findByEmail(principal.getName());
        if (userName.equals(member.getUserName())) {

            PortViewDto portViewDto = portfolioService.findportview(portID);

            model.addAttribute("PortViewDtoList", portViewDto);
            List<ImagesDto> fileDtoList = portfolioService.findportFiles(portID);
            model.addAttribute("FileViewDtoList", fileDtoList);
            List<PortViewDto> portuserList = portfolioService.finduserport(portID);
            model.addAttribute("portuserList", portuserList);

            return "portfolio/portput";
        } else {
            return "error/unAuthorized";
        }

    }


    @PutMapping("/views/put/{PortfolioID}")
    public String portfolioput(@PathVariable("PortfolioID") int PortfolioID, @ModelAttribute PortfolioDto portfolioDto) {

        int uploadResult = portfolioService.UpdatePortfolio(portfolioDto);

        if (uploadResult > 0) {
            return "redirect:/ports/views/" + PortfolioID;
        } else {
            return "redirect:/error/404";
        }
    }

}