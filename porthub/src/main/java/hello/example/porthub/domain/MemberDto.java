package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {

//    private Long UserID;
    private String UserName;
    private String Email;
    private String PasswordHash;
//    private String ProfileImage;
//    private String RegisterationDate;
//    private String AdditionalInfo;
//    private boolean IsAdmin;
//    private boolean IsMento;

}
