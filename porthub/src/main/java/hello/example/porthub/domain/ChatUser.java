package hello.example.porthub.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatUser {
    private int id;
    private String username;
    private String email;
    private String profileImage;

    // Default constructor
    public ChatUser() {
    }

    // Parameterized constructor
    public ChatUser(int id, String username, String email, String profileImage) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
    }
}
