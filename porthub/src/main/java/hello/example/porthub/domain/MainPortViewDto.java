package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MainPortViewDto {
    private int PortfolioID;
    private int AuthorID;
    private int UserID;
    private String Thumbnail_url;
    private String Title;
    private String UserName;
    private String ProfileImage;
}
