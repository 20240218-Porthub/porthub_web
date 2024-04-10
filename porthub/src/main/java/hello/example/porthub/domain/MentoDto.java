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
    private String Department;
    private String CompanyPosition;
    private String CompanyLocation;
    private String CompanyDuration;
    private MultipartFile CareerCertification;
    private String CareerUrl;
    private String UnivName;
    private String Major;
    private String Status;
    private MultipartFile UnivCertification;
    private String UnivUrl;
    private String CertificationName;
    private String IssueDate;
    private String IssuingAuthority;
    private MultipartFile IssueCertification;
    private String IssueUrl;
}
