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
    private int PortfolioID;
    private int AuthorID;
    private String Thumbnail_url;
    private MultipartFile Thumbnail_cast;
    private int CategoryID;
    private String CategoryString;
    private String Title;
    private int Hearts_count = 0;
    private int Views_count = 0;
    private List<MultipartFile> file;
    private List<String> multipleFiles;
    private List<String> content;
    private String AttachmentsOrLinks;

}
