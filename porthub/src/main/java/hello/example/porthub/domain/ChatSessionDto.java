package hello.example.porthub.domain;
import lombok.Data;
import java.util.Date;

@Data  // Lombok's annotation to generate getters, setters, toString, equals, and hashCode methods
public class ChatSessionDto {
    private int sessionID;         // Corresponds to SessionID in the database
    private String sessionName;    // Corresponds to SessionName in the database
    private int createdBy;         // Corresponds to CreatedBy in the database, should reference a valid UserID
    private Date createdAt;        // Corresponds to CreatedAt in the database

    // No need to explicitly write constructors, getters, or setters due to @Data
}
