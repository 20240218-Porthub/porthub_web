package hello.example.porthub.service;

import hello.example.porthub.domain.ChatUsersDto;
import hello.example.porthub.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public int findUserIDByEmail(String email) {
        return userMapper.findUserIDByEmail(email);
    }

    public String findUserProfileImageById(int UserId) {
        return userMapper.findUserProfileImageById(UserId);
    }

    public String findUsernameById(int recipientUserId) { return userMapper.findUsernameById(recipientUserId); }

    public List<ChatUsersDto> getFollowings(int currentUserID) {
        // Loop through the followings list to exclude myself from the list of followings
        List<ChatUsersDto> followings = userMapper.findFollowingsByID(currentUserID);
        for (int i = 0; i < followings.size(); i++) {
            if (followings.get(i).getId() == currentUserID) {
                followings.remove(i);
                break;
            }
        }
        return followings;
    }
}
