package hello.example.porthub.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatUsersDto {
    private int id;
    private String username;
    private String email;
    private String profileImage;

    // Default constructor
    public ChatUsersDto() {
    }

    // Parameterized constructor
    public ChatUsersDto(int id, String username, String email, String profileImage) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
    }
}
