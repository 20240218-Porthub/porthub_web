package hello.example.porthub.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import hello.example.porthub.domain.ChatUser;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT UserID FROM Users WHERE Email = #{currentUserEmail}")
    int findUserIDByEmail(String currentUserEmail);
    /*
    From the User table, fetch the UserID where the Email is equal to the currentUserEmail.
    Then, from the Follows table, fetch the FollowingID where the FollowerID is equal to the UserID fetched from the User table.
    Finally, from the User table, return the ChatUser object where the UserID is equal to the FollowingID fetched from the Follows table.
    */
    @Select("SELECT * FROM Users WHERE UserID IN (SELECT FollowingID FROM Follows WHERE FollowerID = (SELECT UserID FROM Users WHERE Email = #{currentUserEmail}))")
    List<ChatUser> findFollowingsByID(String currentUserEmail);

    @Select("SELECT Username FROM Users WHERE UserID = #{recipientUserId}")
    String findUsernameById(int recipientUserId);
}