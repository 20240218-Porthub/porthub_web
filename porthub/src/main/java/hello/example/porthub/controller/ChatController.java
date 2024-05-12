package hello.example.porthub.controller;

import hello.example.porthub.domain.ChatUser;
import hello.example.porthub.service.ChatService;
import hello.example.porthub.domain.ChatSession;
import hello.example.porthub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<String> handleStartNewChat(@RequestParam("followingUserId") int followingUserId, Principal principal) {
        String currentUserEmail = principal.getName();
        Integer currentUserId = userService.findUserIDByEmail(currentUserEmail);
        boolean isCreated = chatService.startNewChat(currentUserId, followingUserId);
        System.out.println("chats new");
        if (isCreated) {
            return ResponseEntity.ok("Chat started successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start chat");
        }
    }


    @GetMapping("/chats")
    public String chatPage(Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        List<ChatUser> followings = userService.getFollowings(currentUserEmail);
        System.out.println("hi1");
        boolean isNewUser = chatService.getChatSessions(currentUserEmail).isEmpty();
        System.out.println("hi2");
        model.addAttribute("email", currentUserEmail);
        model.addAttribute("followings", followings);
        System.out.println("Followings: " + followings);
        model.addAttribute("chats", chatService.getChatSessions(currentUserEmail));
        model.addAttribute("isNewUser", isNewUser);
        // Add the recipient's username to the model
        if (!isNewUser && !chatService.getChatSessions(currentUserEmail).isEmpty()) {
            // Assuming the first chat session is the selected one for now
            ChatSession selectedChatSession = chatService.getChatSessions(currentUserEmail).get(0);
            model.addAttribute("partnerUsername", chatService.getRecipientUsername(selectedChatSession.getRecipientUserId()));
        }
        return "user/chat";
    }

    @GetMapping("/user/chat")
    public String chatGet(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<ChatSession> chatSessions = chatService.getChatSessions(username);
        model.addAttribute("chatSessions", chatSessions);
        model.addAttribute("name", username);
        return "user/chat";
    }
}
