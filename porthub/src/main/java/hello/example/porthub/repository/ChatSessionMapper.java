package hello.example.porthub.repository;

import hello.example.porthub.domain.ChatSessionDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChatSessionMapper {
//    @Insert("INSERT INTO ChatSessions (SessionName, CreatedBy, CreatedAt) " +
//            "VALUES (#{sessionName}, #{createdBy}, #{createdAt})")
//    @Options(useGeneratedKeys = true, keyProperty = "sessionID")
//    void insertChatSession(ChatSessionDto chatSession);
//
//    @Select("SELECT * FROM ChatSessions WHERE SessionID = #{sessionID}")
//    ChatSessionDto getChatSessionById(int sessionID);
}
