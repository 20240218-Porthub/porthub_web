package hello.example.porthub.repository;

import hello.example.porthub.domain.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByUsername(String username);
    List<ChatSession> findByUsernameAndRecipientUsername(String username, String recipientUsername);
}
