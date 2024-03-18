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
//    private String ProfileImage = null;
//    private String RegisterationDate;
//    private String AdditionalInfo = null;
//    private boolean IsAdmin = false;
//    private boolean IsMento = false;

}
