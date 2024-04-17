package hello.example.porthub.controller;

import hello.example.porthub.domain.ChatUser;
import hello.example.porthub.service.ChatService;
import hello.example.porthub.domain.ChatSession;
import hello.example.porthub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/chats/new")
    public String startNewChat(@RequestParam("followerUsername") String followerUsername, Principal principal) {
        String currentUsername = principal.getName();
        chatService.startNewChat(currentUsername, followerUsername);
        return "redirect:/chats";
    }

    @GetMapping("/chats")
    public String chatPage(Model model, Principal principal) {
        String currentUsername = principal.getName();
        List<ChatUser> followers = userService.getFollowers(Integer.parseInt(currentUsername));
        model.addAttribute("followers", followers);
        return "chat";
    }

    @GetMapping("/user/chat")
    public String chatGet(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<ChatSession> chatSessions = chatService.getChatSessions(username);
        model.addAttribute("chatSessions", chatSessions);
        model.addAttribute("name", username);
        return "chat";
    }
}
