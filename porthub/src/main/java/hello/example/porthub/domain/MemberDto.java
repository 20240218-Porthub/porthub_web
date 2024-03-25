package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {

    private String UserName;
    private String Email;
    private String PasswordHash;
    private String Role;

}
