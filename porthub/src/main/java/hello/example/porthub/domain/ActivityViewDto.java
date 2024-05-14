package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ActivityViewDto {
    private int MentoringID;
    private String UserName;
    private String ProfileImage;
    private String Title;
    private String file_urls;
    private List<String> urls;
}
