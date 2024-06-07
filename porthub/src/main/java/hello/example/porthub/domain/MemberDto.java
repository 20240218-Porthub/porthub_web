package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {
    @Getter
    private int UserID;
    @Getter
    private String UserName;
    private String Email;
    private String ProfileImage;
    private String backImage;
    private String PasswordHash;
    private String Role;
    private String PaidProduct;
}
