package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PopularDto {

    private int UserID;
    private String UserName;
    private String Email;
    private String ProfileImage;
    private String aff;
    private int FollowingID;
    private boolean followCheck;
    //follower id는 UserID이고 FollowingID를 찾아서 체크를 해주어야 함
}