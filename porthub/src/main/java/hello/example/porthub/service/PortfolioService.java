package hello.example.porthub.service;

import hello.example.porthub.domain.CategoryDto;
import hello.example.porthub.domain.ImagesDto;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.PortfolioDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final MemberService memberService;
    private final ImagesDto imagesDto;

    public int upload(PortfolioDto portfolioDto) throws IOException {
        String Category = portfolioDto.getCategoryString();
        String thumbnailUrl = s3Service.uploadFiles(portfolioDto.getThumbnail_cast());

        portfolioDto.setThumbnail_url(thumbnailUrl);



// UserID 가져오기
        String Email = memberService.getCurrentUserId();
        System.out.println(memberRepository.findByUserIDtoEmail(Email));

        portfolioDto.setAuthorID(memberRepository.findByUserIDtoEmail(Email).getUserID());
// 포트폴리오 카테고리와 해당하는 값 매핑
        Map<String, Integer> categoryMap = new HashMap<>();
        categoryMap.put("Development", 2);
        categoryMap.put("Music", 3);
        categoryMap.put("Design", 4);
        categoryMap.put("Editing", 5);
        categoryMap.put("Film", 6);
        categoryMap.put("Marketing", 7);
        categoryMap.put("Other", 8);

        int CategoryID = categoryMap.getOrDefault(Category, -1).intValue();
        portfolioDto.setCategoryID(CategoryID);

        // portfolioID를 가져옴 Images Table에 넣어주기 위함
        int PortfolioID = portfolioRepository.PortUpload(portfolioDto);

        imagesDto.setPortfolioID(PortfolioID);


        List<String> multipleFiles = new ArrayList<>();
        List<String> contentList = portfolioDto.getContent();

        if (portfolioDto.getFile() != null) {
            for (MultipartFile file : portfolioDto.getFile()) {
                String multipleFile = s3Service.uploadFiles(file);
                multipleFiles.add(multipleFile);
            }
        }

        portfolioDto.setMultipleFiles(multipleFiles); // List<String>

        int UploadSize = multipleFiles.size();

        if (contentList != null && multipleFiles != null) {
            for (int i = 0; i < UploadSize; i++) {
                imagesDto.setImage_url(multipleFiles.get(i));
                imagesDto.setContents(contentList.get(i));
                portfolioRepository.ContentUpload(imagesDto);
                System.out.println(i);
                System.out.println(imagesDto);
            }
        }


        return 1;
    }

    public List<CategoryDto> findByCategory() {
        return portfolioRepository.findByCategory();
    }

}
