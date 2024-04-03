package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileDto {
    private int UserID;
    private String intro;
    private String aff;
    private String link;
    private String career;
}
