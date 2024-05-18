package hello.example.porthub.repository;

import hello.example.porthub.domain.ChatMessageDto;
import hello.example.porthub.domain.ChatSessionDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMapper {
    @Select("SELECT DISTINCT SessionID FROM Chats WHERE SenderID = #{userId} OR ReceiverID = #{userId}")
    List<String> getDistinctSessionKeysForUser(int userId);

    @Select("SELECT * FROM Chats WHERE SessionID = #{sessionId} ORDER BY DateTime DESC LIMIT 1")
    @Results({
            @Result(property = "id", column = "ChatID"),
            @Result(property = "senderUserId", column = "SenderID"),
            @Result(property = "recipientUserId", column = "ReceiverID"),
            @Result(property = "content", column = "Content"),
            @Result(property = "timestamp", column = "DateTime"),
            @Result(property = "sessionId", column = "SessionID")
    })
    ChatMessageDto getLatestMessageForSession(String sessionId);

    /* Fetch the last message and the timestamp of the ChatMessageDto between the current user and the given recipient userID.
        SELECT c.Content AS lastMessage, c.DateTime AS lastMessageTimestamp
        FROM Chats c
        WHERE (c.SenderID = :currentUserId AND c.ReceiverID = :recipientUserId)
        OR (c.SenderID = :recipientUserId AND c.ReceiverID = :currentUserId)
        ORDER BY c.DateTime DESC
        LIMIT 1;
     */
    @Select("SELECT * FROM Chats " +
            "WHERE (SenderID = #{currentUserId} AND ReceiverID = #{recipientUserId}) " +
            "   OR (SenderID = #{recipientUserId} AND ReceiverID = #{currentUserId}) " +
            "ORDER BY DateTime DESC " +
            "LIMIT 1")
    ChatMessageDto findLastChatSessionBetweenUsers(@Param("currentUserId") int currentUserId, @Param("recipientUserId") int recipientUserId);

    @Select("SELECT * FROM Chats " +
            "WHERE (SenderID = #{currentUserId} AND ReceiverID = #{recipientUserId}) " +
            "   OR (SenderID = #{recipientUserId} AND ReceiverID = #{currentUserId}) " +
            "ORDER BY DateTime DESC ")
    List<ChatMessageDto> findAllChatSessionsBetweenUsers(int currentUserID, int followingUserID);

    // Insert a new chat session into the database
    @Insert("INSERT INTO Chats (SenderID, ReceiverID, Content, DateTime, SessionID) " +
            "VALUES (#{senderUserId}, #{recipientUserId}, #{content}, #{timestamp}, #{sessionId})")
    void insertChatMessage(ChatMessageDto chatMessage);
}
