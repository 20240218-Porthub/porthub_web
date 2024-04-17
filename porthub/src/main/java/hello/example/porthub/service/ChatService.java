package hello.example.porthub.service;

import hello.example.porthub.domain.ChatSession;
import hello.example.porthub.repository.ChatMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public void startNewChat(String currentUsername, String followerUsername) {
        // Check if a chat already exists between the two users
        List<ChatSession> existingChats = chatMapper.findByUsernameOrRecipientUsername(currentUsername);
        boolean chatExists = existingChats.stream()
                .anyMatch(chat -> chat.getUsername().equals(followerUsername) || chat.getRecipientUsername().equals(followerUsername));
        if (!chatExists) {
            // Create a new chat session
            ChatSession newChat = new ChatSession();
            // Set the properties using the generated setter methods
            newChat.setId(null);
            newChat.setUsername(currentUsername);
            newChat.setRecipientUsername(followerUsername);
            newChat.setLastMessage("");
            newChat.setLastMessageTimestamp(new Date());
            chatMapper.insertChatSession(newChat);
        }
    }
}