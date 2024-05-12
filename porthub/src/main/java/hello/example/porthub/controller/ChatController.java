package hello.example.porthub.controller;

import hello.example.porthub.domain.ChatUser;
import hello.example.porthub.domain.ChatSessionDto;
import hello.example.porthub.service.ChatService;
import hello.example.porthub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @MessageMapping


    @PostMapping("/chats/new")
    public ResponseEntity<String> handleNewMessage(@RequestBody ChatSessionDto chatSession, Principal principal) {
        // Check if the principal is null, indicating no authenticated user.
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // Validate that the recipient user ID is provided.
        if (chatSession.getRecipientUserId() == null) {
            return ResponseEntity.badRequest().body("Recipient ID is required");
        }

        // Additional validation checks can be added here, e.g., content validation.
        if (chatSession.getContent() == null || chatSession.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body("Message content is required");
        }

        // Try to create a new chat session using the provided information.
        try {
            String currentUserEmail = principal.getName();
            int currentUserId = userService.findUserIDByEmail(currentUserEmail);
            chatService.createNewChatSession(currentUserId, chatSession.getRecipientUserId(), chatSession.getContent());
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            // Log the exception details here for debugging purposes.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending message: " + e.getMessage());
        }
    }

    @GetMapping("/user/chat")
    public String chatPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String currentUserEmail = principal.getName();
        int currentUserId = userService.findUserIDByEmail(currentUserEmail);
        List<ChatUser> followings = userService.getFollowings(currentUserId);
        List<ChatSessionDto> chatSessions = chatService.getFullChatHistoryForUser(currentUserId);
        System.out.println(chatSessions);
        model.addAttribute("email", currentUserEmail);
        model.addAttribute("userID", currentUserId);
        model.addAttribute("followings", followings);
        model.addAttribute("chats", chatSessions);
        return "user/chat";
    }
}
