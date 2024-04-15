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

        //session에 필요한 저보 저장
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

    @GetMapping("/checkHeart/{portfolioID}/{authorID}")
    @ResponseBody
    public Map<String, Boolean> checkHeart(@PathVariable int portfolioID, @PathVariable String authorID) {
        Map<String, Boolean> response = new HashMap<>();

        // Heart_Check 확인
        boolean heartCheck = portfolioService.checkHeart(portfolioID, authorID);

        heartCheck = true;
        response.put("heartCheck", heartCheck);

        return response;
    }


}