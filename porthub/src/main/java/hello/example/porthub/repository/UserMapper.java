package hello.example.porthub.repository;

import hello.example.porthub.domain.ChatUsersDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT UserID as id, UserName as username, Email, ProfileImage FROM Users WHERE Email = #{currentUserEmail}")
    int findUserIDByEmail(String currentUserEmail);

    @Select("SELECT UserID as id, UserName as username, Email, ProfileImage FROM Users WHERE UserID IN (SELECT FollowingID FROM Follows WHERE FollowerID = #{id})")
    List<ChatUsersDto> findFollowingsByID(int id);

    @Select("SELECT UserName FROM Users WHERE UserID = #{recipientUserId}")
    String findUsernameById(int recipientUserId);
}
