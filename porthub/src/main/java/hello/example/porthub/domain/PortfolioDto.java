package hello.example.porthub.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class PortfolioDto {
    private MultipartFile Thumbnail_url;
    private String CategoryID;
    private String Title;
    private List<MultipartFile> file;
    private List<String> content;
    private String AttachmentsOr;

}
