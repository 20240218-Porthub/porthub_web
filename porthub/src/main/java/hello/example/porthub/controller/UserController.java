package hello.example.porthub.controller;

import hello.example.porthub.domain.ChatUser;
import hello.example.porthub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/followers")
    public List<ChatUser> getFollowers(Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return userService.getFollowers(userId);
    }

    private int getUserIdFromPrincipal(Principal principal) {
        // Assuming principal.getName() returns the user's ID as a string
        return Integer.parseInt(principal.getName());
    }
}
