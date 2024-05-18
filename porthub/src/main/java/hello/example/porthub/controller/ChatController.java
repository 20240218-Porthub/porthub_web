package hello.example.porthub.controller;

import hello.example.porthub.config.util.ChatSessionUtil;
import hello.example.porthub.domain.ChatMessageDto;
import hello.example.porthub.domain.ChatUsersDto;
import hello.example.porthub.service.ChatService;
import hello.example.porthub.service.SessionParticipantService;
import hello.example.porthub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SessionParticipantService sessionParticipantService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService, SessionParticipantService sessionParticipantService) {
        this.chatService = chatService;
        this.userService = userService;
        this.sessionParticipantService = sessionParticipantService;
    }

//    @MessageMapping("/chat/send")
//    @SendTo("/topic/messages")
//    public sendMessage handleMessage(ChatMessage message) throws Exception {
//        chatService.saveMessage(message);
//        return message;
//    }

    @PostMapping("/chats/new")
    public ResponseEntity<String> handleNewMessage(@RequestBody ChatMessageDto chatSession, Principal principal) {
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
            String sessionKey = ChatSessionUtil.generateSessionKey(currentUserId, chatSession.getRecipientUserId());
            sessionParticipantService.addParticipantToSession(sessionKey, currentUserId);
            sessionParticipantService.addParticipantToSession(sessionKey, chatSession.getRecipientUserId());
            chatService.saveMessages(currentUserId, chatSession.getRecipientUserId(), chatSession.getContent(), sessionKey);
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
        List<ChatUsersDto> followings = userService.getFollowings(currentUserId);
        List<ChatMessageDto> chatSessions = chatService.getFullChatHistoryForUser(currentUserId);
        model.addAttribute("email", currentUserEmail);
        model.addAttribute("userID", currentUserId);
        model.addAttribute("followings", followings);
        model.addAttribute("chatSessions", chatSessions);
        return "user/chat";
    }
}
