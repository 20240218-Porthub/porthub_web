package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class MentoringDto {
    private int MentoringID;
    private int MentoID;
    private String CategoryString;
    private int CategoryID;
    private String Title;
    private float Price;
    private MultipartFile Thumbnailfile;
    private String Thumbnail;
    private List<MultipartFile> mentofile;
    private String Content;
    private String file_urls;
    private int mentoring_delete;

}
