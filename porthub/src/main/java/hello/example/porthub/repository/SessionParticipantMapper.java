package hello.example.porthub.repository;

import hello.example.porthub.domain.SessionParticipantDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SessionParticipantMapper {

    @Insert("INSERT INTO SessionParticipants (SessionKey, UserID) VALUES (#{sessionKey}, #{userId})")
    void insertSessionParticipant(@Param("sessionKey") String sessionKey, @Param("userId") int userId);

    @Select("SELECT COUNT(*) FROM SessionParticipants WHERE SessionKey = #{sessionKey} AND UserID = #{userId}")
    boolean isSessionParticipant(@Param("sessionKey") String sessionKey, @Param("userId") int userId);

    @Select("SELECT UserID FROM SessionParticipants WHERE SessionKey = #{sessionKey} AND UserID != #{userId}")
    Integer findOtherParticipant(String sessionKey, int userId);

    @Select("SELECT SessionKey FROM SessionParticipants WHERE UserID = #{userId}")
    List<String> findSessionKeysByUserId(int userId);
}
