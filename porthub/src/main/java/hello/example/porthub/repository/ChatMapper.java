package hello.example.porthub.repository;

import hello.example.porthub.domain.ChatSession;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMapper {
    // Fetch chat sessions from the database where the UserID is equal to the given UserID or the recipient UserID is equal to the given UserID.
    @Select("SELECT * FROM Chats WHERE SenderID = #{userID} OR ReceiverID = #{userID}")
    List<ChatSession> findByUsernameOrRecipientUsername(@Param("userID") int userID);

    /* Fetch the last message and the timestamp of the ChatSession between the current user and the given recipient userID.
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
    ChatSession findLastChatSessionBetweenUsers(@Param("currentUserId") int currentUserId, @Param("recipientUserId") int recipientUserId);

    // Insert a new chat session into the database
    @Insert("INSERT INTO Chats (SenderID, ReceiverID, Content, DateTime) " +
            "VALUES (#{senderUserId}, #{recipientUserId}, #{content}, #{timestamp})")
    void insertChatSession(ChatSession chatSession);
}
