package hello.example.porthub.domain;

import lombok.Data;

@Data
public class SessionParticipantDto {
    private String sessionKey;
    private int userId;
}
