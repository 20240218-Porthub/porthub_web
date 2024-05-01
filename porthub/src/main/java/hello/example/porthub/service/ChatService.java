package hello.example.porthub.service;

import hello.example.porthub.domain.ChatSession;
import hello.example.porthub.repository.ChatMapper;
import hello.example.porthub.repository.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChatService {
    private final ChatMapper chatMapper;
    private final UserMapper userMapper;

    public ChatService(ChatMapper chatMapper, UserMapper userMapper) {
        this.chatMapper = chatMapper;
        this.userMapper = userMapper;
    }

    // Method to fetch chat sessions from the database
    public List<ChatSession> getChatSessions(String userEmail) {
        Integer userId = userMapper.findUserIDByEmail(userEmail);
        return chatMapper.findByUsernameOrRecipientUsername(userId);
    }

    // Method to fetch the last message and timestamp for each chat session
    public List<ChatSession> getLastMessageAndTimestampForUser(int userId, int recipientUserId) {
        return (List<ChatSession>) chatMapper.findLastChatSessionBetweenUsers(userId, recipientUserId);
    }

    // Method to fetch the recipient's username of the current chat session
    public String getRecipientUsername(int recipientUserId) {
        return userMapper.findUsernameById(recipientUserId);
    }

    public boolean startNewChat(Integer currentUserID, Integer followingUserID) {
        // Check if a chat already exists between the two users
        List<ChatSession> existingChats = chatMapper.findByUsernameOrRecipientUsername(currentUserID);
        boolean chatExists = existingChats.stream()
                .anyMatch(chat -> chat.getSenderUserId().equals(followingUserID) || chat.getRecipientUserId().equals(followingUserID));
        if (!chatExists) {
            // Create a new chat session
            ChatSession newChat = new ChatSession();
            // Set the properties using the generated setter methods
            newChat.setId(null);
            newChat.setSenderUserId(currentUserID);
            newChat.setRecipientUserId(followingUserID);
            newChat.setContent("");
            newChat.setTimestamp(new Date());
            chatMapper.insertChatSession(newChat);
        }
        return chatExists;
    }
}
