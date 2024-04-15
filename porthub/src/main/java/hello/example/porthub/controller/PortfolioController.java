package hello.example.porthub.controller;
import hello.example.porthub.config.util.SessionUtils;
import hello.example.porthub.domain.*;
import hello.example.porthub.service.PortfolioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/ports")
public class PortfolioController {

    private final PortfolioService portfolioService;

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
    public String portfolioDetail(@PathVariable(name = "PortfolioID") int PortfolioID, Model model, HttpSession session) {


        PortViewDto portViewDto = portfolioService.findportview(PortfolioID);
        model.addAttribute("PortViewDtoList", portViewDto);
        System.out.println(portViewDto);
        List<ImagesDto> fileDtoList = portfolioService.findportFiles(PortfolioID);
        model.addAttribute("FileViewDtoList", fileDtoList);
        System.out.println("hi" + fileDtoList);
        List<PortViewDto> portuserList = portfolioService.finduserport(PortfolioID);
        model.addAttribute("portuserList", portuserList);

        if (SessionUtils.isLoggedIn()) {
            System.out.println("login되었음");
            model.addAttribute("isLoggedIn", true);
            //로그인 되어있는 경우 사용자 아이디
        } else {
            System.out.println("logout되어있음");
            model.addAttribute("isLoggedIn", false);
            //not login not session
        }

        boolean heartCheck = portfolioService.checkHeart(PortfolioID, SessionUtils.getCurrentUsername());
        model.addAttribute("heartCheck", heartCheck);


        //session에 필요한 저보 저장
        session.setAttribute("heartCheck", heartCheck);
        session.setAttribute("portfolioID", portViewDto.getPortfolioID());
        session.setAttribute("authorID", portViewDto.getAuthorID());

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


        System.out.println(copyrightReportDto);
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
        } else {
            portLikeDto.setHeart_Check(true);
        }
        //db에서 데이터 없을 경우 default = 0으로 insert하고 데이터 존재하는 경우 Check_Heart 역전 시키기
//        설정값대로
        portfolioService.convertLikes(portLikeDto);

        System.out.println(portLikeDto);


        return "redirect:/ports/views/" + portfolioID;
    }



}