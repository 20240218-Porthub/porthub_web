package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalculatePopularDto {
    private int AuthorID;
    private int Hearts_count;
    private int Views_count;
}
