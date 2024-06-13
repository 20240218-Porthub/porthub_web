package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CopyrightReportDto {
    private int ReportID;
    private int PortfolioID;
    private String Contents;
    private String ReporterEmail;
    private String ReporterName;
    private String ReportedName;
    private int ReportedID;
}
