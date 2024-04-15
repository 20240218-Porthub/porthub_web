package hello.example.porthub.repository;

import hello.example.porthub.domain.ChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMapper {
    @Select("SELECT * FROM Chats WHERE SenderID = #{username} OR ReceiverID = #{username}")
    List<ChatSession> findByUsernameOrRecipientUsername(@Param("username") String username);

    @Select("SELECT c1.Content, c1.DateTime " +
            "FROM Chats c1 " +
            "INNER JOIN (" +
            "    SELECT MAX(ChatID) AS LastChatID " +
            "    FROM Chats " +
            "    WHERE SenderID = #{userId} OR ReceiverID = #{userId} " +
            "    GROUP BY LEAST(SenderID, ReceiverID), GREATEST(SenderID, ReceiverID)" +
            ") c2 ON c1.ChatID = c2.LastChatID")
    List<ChatSession> findLastMessageAndTimestampForUser(@Param("userId") Long userId);
}