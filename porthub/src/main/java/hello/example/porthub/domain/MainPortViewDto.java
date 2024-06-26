package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class MainPortViewDto {
    private int PortfolioID;
    private int AuthorID;
    private int UserID;
    private int CategoryID;
    private String Thumbnail_url;
    private String Title;
    private String UserName;
    private String ProfileImage;
}
