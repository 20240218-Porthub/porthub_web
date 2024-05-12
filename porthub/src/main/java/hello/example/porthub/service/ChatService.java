package hello.example.porthub.service;

import hello.example.porthub.domain.ChatSessionDto;
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

    // Method to create a new chat session (message)
    public void createNewChatSession(Integer senderId, Integer recipientId, String content) {
        if (!isValidUser(senderId) || !isValidUser(recipientId)) {
            throw new IllegalArgumentException("One or both user IDs are invalid or do not exist.");
        }
        ChatSessionDto newChat = new ChatSessionDto();
        newChat.setSenderUserId(senderId);
        newChat.setRecipientUserId(recipientId);
        newChat.setContent(content);
        newChat.setTimestamp(new Date());
        chatMapper.insertChatSession(newChat);
    }

    // Use this method to get the full chat history
    public List<ChatSessionDto> getChatSessions(int currentUserID, int followingUserID) {
        return chatMapper.findAllChatSessionsBetweenUsers(currentUserID, followingUserID);
    }

    // Use this method to get the most recent chat session
    // If the chat session does not exist, it will return null
    public ChatSessionDto getLastChatSession(int currentUserID, int followingUserID) {
        return chatMapper.findLastChatSessionBetweenUsers(currentUserID, followingUserID);
    }

    private boolean isValidUser(Integer userID) {
        return userMapper.findUsernameById(userID) != null;
    }

    public List<ChatSessionDto> getFullChatHistoryForUser(Integer currentUserId) {
        return chatMapper.findByUsernameOrRecipientUsername(currentUserId);
    }
}
