package hello.example.porthub.controller;

import hello.example.porthub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/followers")
//    public List<ChatUser> getFollowers(ChatUser chatUser, Principal principal) {
//        int userId = chatUser.getId();
//        String name = principal.getName();
//        return userService.getFollowings(name);
//    }
    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getLoggedInUser(Principal principal) {
        String username = principal.getName();
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        return ResponseEntity.ok(response);
    }
}