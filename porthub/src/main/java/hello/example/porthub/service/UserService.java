package hello.example.porthub.service;

import hello.example.porthub.domain.ChatUser;
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

    public List<ChatUser> getFollowers(int userId) {
        return userMapper.findFollowersById(userId);
    }
}
