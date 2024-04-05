package hello.example.porthub.controller;
import hello.example.porthub.domain.PortfolioDto;
import hello.example.porthub.service.PortfolioService;
import hello.example.porthub.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/ports")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final S3Service s3Service;

    @PostMapping("/upload")
    public String uploadPortfolio(@ModelAttribute PortfolioDto portfolioDto) throws IOException {
        System.out.println("portfolioDto= " + portfolioDto);
        System.out.println("portfolioimage,content" + portfolioDto.getContent() + portfolioDto.getFile());
        String thumbnailUrl = s3Service.uploadThumbnail(portfolioDto.getThumbnail_url());
        System.out.println(thumbnailUrl);
        // 이 경로를 service단에 전달해서 db에 저장해야함. repository상에서 Multiple 타입으로 작성되어있음
        int uploadResult = portfolioService.upload(portfolioDto);
        if (uploadResult > 0) {
            return "redirect:/portfolio/main";
        } else {
            return "redirect:/portfolio/ports/create";
        }
    }

    @GetMapping("/create")
    public String create() {
        return "portfolio/create";
    }

}