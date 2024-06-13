package hello.example.porthub.domain.AdminDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAdminDto {

    private int UserID;
    private String UserName;
    private String Email;
    private String ProfileImage;
    private String backImage;
}
