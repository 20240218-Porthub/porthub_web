package hello.example.porthub.service;

import hello.example.porthub.domain.ChatSession;
import hello.example.porthub.repository.ChatMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatMapper chatMapper;

    public ChatService(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    // Method to fetch chat sessions from the database
    public List<ChatSession> getChatSessions(String username) {
        return chatMapper.findByUsernameOrRecipientUsername(username);
    }

    // Method to fetch the last message and timestamp for each chat session
    public List<ChatSession> getLastMessageAndTimestampForUser(Long userId) {
        return chatMapper.findLastMessageAndTimestampForUser(userId);
    }
}