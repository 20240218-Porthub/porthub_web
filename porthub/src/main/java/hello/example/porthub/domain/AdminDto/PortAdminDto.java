package hello.example.porthub.domain.AdminDto;




import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PortAdminDto {
    private int PortfolioID;
    private int AuthorID;
    private String UserName;
    private String Thumbnail_url;
    private int CategoryID;
    private String Title;
}
