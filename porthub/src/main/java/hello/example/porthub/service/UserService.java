package hello.example.porthub.service;

import hello.example.porthub.domain.ChatUsersDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final MemberRepository memberRepository;

    @Autowired
    public UserService(UserMapper userMapper, MemberRepository memberRepository) {
        this.userMapper = userMapper;
        this.memberRepository = memberRepository;
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
        // Always add GPT-4O to the list of followings
        followings.add(userMapper.findChatUserDtoByUserID(-2));
        for (int i = 0; i < followings.size(); i++) {
            if (followings.get(i).getId() == currentUserID) {
                followings.remove(i);
                break;
            }
        }
        System.out.println(followings);
        return followings;
    }
}
