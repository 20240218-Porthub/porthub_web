package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MentoProcessDto {
    private int ProcessID;
    private int MentoID;
    private String Process;
}
