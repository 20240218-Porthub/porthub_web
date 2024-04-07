package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ToString
@Component
public class ImagesDto {
    private int PortfolioID;
    private String Image_url;
    private String contents;

}
