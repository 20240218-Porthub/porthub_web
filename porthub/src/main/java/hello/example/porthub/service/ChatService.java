package hello.example.porthub.service;

import hello.example.porthub.domain.ChatMessageDto;
import hello.example.porthub.repository.ChatMapper;
import hello.example.porthub.repository.SessionParticipantMapper;
import hello.example.porthub.repository.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class ChatService {
    private final ChatMapper chatMapper;
    private final UserMapper userMapper;
    private final SessionParticipantMapper sessionParticipantMapper;

    public ChatService(ChatMapper chatMapper, UserMapper userMapper, SessionParticipantMapper sessionParticipantMapper) {
        this.chatMapper = chatMapper;
        this.userMapper = userMapper;
        this.sessionParticipantMapper = sessionParticipantMapper;
    }

    public void saveMessages(Integer senderId, Integer recipientId, String content, String sessionKey) {
        if (!isValidUser(senderId) || !isValidUser(recipientId)) {
            throw new IllegalArgumentException("One or both user IDs are invalid or do not exist.");
        }
        // Check if there's an existing chat session between the two users
        ChatMessageDto newChat = new ChatMessageDto();
        newChat.setSenderUserId(senderId);
        newChat.setRecipientUserId(recipientId);
        newChat.setContent(content);
        newChat.setTimestamp(new Date());
        newChat.setSessionId(sessionKey);
        chatMapper.insertChatMessage(newChat);
    }

    public List<ChatMessageDto> getFullChatHistoryForUser(int userId) {
        List<String> sessionKeys = chatMapper.getDistinctSessionKeysForUser(userId);
        List<ChatMessageDto> chatSummaries = new ArrayList<>();

        for (String sessionKey : sessionKeys) {
            ChatMessageDto latestMessage = chatMapper.getLatestMessageForSession(sessionKey);
            if (latestMessage != null) {
                chatSummaries.add(new ChatMessageDto(latestMessage.getId(),
                        userId,
                        sessionParticipantMapper.findOtherParticipant(sessionKey, userId),
                        latestMessage.getContent(),
                        latestMessage.getTimestamp(),
                        sessionKey));
            }
        }

        // Sort chatSummaries by timestamp in descending order so that the most recent chats appear first
        chatSummaries.sort(Comparator.comparing(ChatMessageDto::getTimestamp).reversed());

        return chatSummaries;
    }


    private boolean isValidUser(Integer userID) {
        return userMapper.findUsernameById(userID) != null;
    }

    public List<ChatMessageDto> getChatHistoryBySessionId(String sessionId, int currentUserId) {
        List<ChatMessageDto> preprocessChatMessage =  chatMapper.getChatHistoryBySessionId(sessionId);
        List<ChatMessageDto> chatMessages = new ArrayList<>();
        for (ChatMessageDto chatMessage : preprocessChatMessage) {
            if (chatMessage.getSenderUserId() == currentUserId || chatMessage.getRecipientUserId() == currentUserId) {
                chatMessages.add(chatMessage);
            }
        }
        return chatMessages;
    }

    public int getRecipientUserIdBySessionId(String sessionId, int currentUserId) {
        // Logic to retrieve the recipient user's ID based on the sessionId and currentUserId
        return sessionParticipantMapper.findOtherParticipant(sessionId, currentUserId);
    }

    public boolean isSessionParticipant(String sessionId, int userId) {
        return sessionParticipantMapper.isSessionParticipant(sessionId, userId);
    }

    public void leaveChatSession(String sessionId, int currentUserId) {
        chatMapper.updateUserToLeft(sessionId, currentUserId);
    }
}
