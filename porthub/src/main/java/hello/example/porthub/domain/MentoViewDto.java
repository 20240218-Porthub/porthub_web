package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MentoViewDto {
    private int UserID;
    private int CategoryID;
    private String UserName;
    private String profileImage;
    private int MentoringID;
    private float Price;
    private String Title;
    private String Thumbnail;
    private int mentoring_delete;
}
