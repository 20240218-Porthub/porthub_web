package hello.example.porthub.controller;

import hello.example.porthub.service.ChatService;
import hello.example.porthub.domain.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
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