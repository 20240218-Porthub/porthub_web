package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class MentoDto {
    private int MentoID;
    private int UserID;
    private String Introduction;
    private String Ability;
    private String CompanyName;
    private MultipartFile CareerFiles;
    private String CareerCertification;
    private String UnivName;
    private MultipartFile UnivFiles;
    private String UnivCertification;
    private String CertificationName;
    private MultipartFile IssueFiles;
    private String IssueCertification;
    private int credit;
}
