package hello.example.porthub.service;

import hello.example.porthub.domain.ChatMessageDto;
import hello.example.porthub.repository.ChatMapper;
import hello.example.porthub.repository.SessionParticipantMapper;
import hello.example.porthub.repository.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                System.out.println("Latest message: " + latestMessage);
                chatSummaries.add(new ChatMessageDto(latestMessage.getId(),
                        userId,
                        sessionParticipantMapper.findOtherParticipant(sessionKey, userId),
                        latestMessage.getContent(),
                        latestMessage.getTimestamp(),
                        sessionKey));
            }
        }
        return chatSummaries;
    }

    private boolean isValidUser(Integer userID) {
        return userMapper.findUsernameById(userID) != null;
    }
}
