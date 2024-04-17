package hello.example.porthub.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import hello.example.porthub.domain.ChatUser;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT u.* FROM Users u JOIN Follows f ON u.UserID = f.FollowingID WHERE f.FollowerID = #{userId}")
    List<ChatUser> findFollowersById(int userId);
}