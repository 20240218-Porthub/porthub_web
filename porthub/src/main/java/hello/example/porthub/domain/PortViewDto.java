package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@Component
public class PortViewDto {
    private int PortfolioID;
    private int AuthorID;
    private String Thumbnail_url;
    private int CategoryID;
    private String Title;
    private int Views_count;
    private String CreationDate;
    private String AttachmentsOrLinks;
    private String UserName;
    private String ProfileImage;
    private String backImage;

}
