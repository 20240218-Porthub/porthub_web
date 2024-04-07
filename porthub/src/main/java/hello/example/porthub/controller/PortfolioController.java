package hello.example.porthub.controller;
import hello.example.porthub.domain.PortfolioDto;
import hello.example.porthub.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/ports")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/uploads")
    public String uploadPortfolio( @ModelAttribute PortfolioDto portfolioDto) throws IOException {

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

}