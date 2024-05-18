package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ChatMessageDto {
    private Long id;
    private Integer senderUserId;
    private Integer recipientUserId;
    private String content;
    private Date timestamp;
    private String sessionId;

    public ChatMessageDto(Long id, Integer senderUserId, Integer recipientUserId, String content, Date timestamp, String sessionId) {
        this.id = id;
        this.senderUserId = senderUserId;
        this.recipientUserId = recipientUserId;
        this.content = content;
        this.timestamp = timestamp;
        this.sessionId = sessionId;
    }

    public ChatMessageDto() {
    }
}
