package hello.example.porthub.controller;
import hello.example.porthub.domain.PortfolioDto;
import hello.example.porthub.service.PortfolioService;
import hello.example.porthub.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
        // form data 전송시 List 형태로 담아두었음. getContent(), getFile()시 여러개의 파일이 담겨있음
        // 이를 순서대로 db에 저장하는 로직을 구현해야 함.
        String thumbnailUrl = s3Service.uploadFiles(portfolioDto.getThumbnail_url());
        // 이 경로를 service단에 전달해서 db에 저장해야함. repository상에서 Multiple 타입으로 작성되어있음
        List<String> multipleFiles = new ArrayList<>();

        for (MultipartFile file : portfolioDto.getFile()) {
            String multipleFile = s3Service.uploadFiles(file);
            multipleFiles.add(multipleFile);
        }


        System.out.println(thumbnailUrl);
        System.out.println("url_set"+ multipleFiles);

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