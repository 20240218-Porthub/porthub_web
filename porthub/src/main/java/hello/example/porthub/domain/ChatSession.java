package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ChatSession {
    private Long id;
    private String username;
    private String recipientUsername;
    private String lastMessage;
    private Date lastMessageTimestamp;

    public ChatSession(Long id, String username, String recipientUsername, String lastMessage, Date lastMessageTimestamp) {
        this.id = id;
        this.username = username;
        this.recipientUsername = recipientUsername;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public ChatSession() {

    }
}