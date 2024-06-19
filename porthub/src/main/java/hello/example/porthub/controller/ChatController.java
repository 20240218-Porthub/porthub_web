package hello.example.porthub.controller;

import hello.example.porthub.service.ChatGptService;
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
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SessionParticipantService sessionParticipantService;
    private final ChatGptService chatGptService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(ChatService chatService, UserService userService,
                          SessionParticipantService sessionParticipantService,
                          ChatGptService chatGptService,
                          SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.userService = userService;
        this.sessionParticipantService = sessionParticipantService;
        this.chatGptService = chatGptService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessage) {
        Integer senderId = chatMessage.getSenderUserId();
        Integer recipientId = chatMessage.getRecipientUserId();
        String content = chatMessage.getContent();
        String sessionId = chatMessage.getSessionId();

        chatService.saveMessages(senderId, recipientId, content, sessionId);

        if (recipientId != null && recipientId == -2) {
            String gptResponse = chatGptService.sendGpt(content);
            chatService.saveMessages(recipientId, senderId, gptResponse, sessionId);

            ChatMessageDto responseMessage = new ChatMessageDto();
            responseMessage.setSenderUserId(-2);
            responseMessage.setRecipientUserId(senderId);
            responseMessage.setContent(gptResponse);
            responseMessage.setSessionId(sessionId);

            // GPT 응답을 WebSocket을 통해 클라이언트로 전송
            messagingTemplate.convertAndSend("/topic/public", responseMessage);
            // return responseMessage;
            return null;
        } else {
            return chatMessage;
        }
    }

    @GetMapping("/api/chat-messages/{sessionId}")
    @ResponseBody
    public ResponseEntity<Object> getChatMessagesBySessionId(@PathVariable("sessionId") String sessionId, Principal principal) {
        try {
            String currentUserEmail = principal.getName();
            int currentUserId = userService.findUserIDByEmail(currentUserEmail);
            List<ChatMessageDto> chatMessages = chatService.getChatHistoryBySessionId(sessionId, currentUserId);
            return ResponseEntity.ok(chatMessages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to load chat messages"));
        }
    }

    @PostMapping("/chats/new")
    public ResponseEntity<String> handleNewMessage(@RequestBody ChatMessageDto chatSession, Principal principal) {
        System.out.println("New message received: " + chatSession);
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        if (chatSession.getRecipientUserId() == null) {
            return ResponseEntity.badRequest().body("Recipient ID is required");
        }
        if (chatSession.getContent() == null || chatSession.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body("Message content is required");
        }
        try {
            String currentUserEmail = principal.getName();

            int currentUserId = userService.findUserIDByEmail(currentUserEmail); // 이메일로부터 현재 사용자 ID를 가져옴
            String sessionId = ChatSessionUtil.generateSessionKey(currentUserId, chatSession.getRecipientUserId()); // 세션 ID 생성
            Integer recipientId = chatSession.getRecipientUserId();
            sessionParticipantService.addParticipantToSession(sessionId, currentUserId);
            sessionParticipantService.addParticipantToSession(sessionId, chatSession.getRecipientUserId());

            // 메시지를 GPT-4로 보내고 응답을 받아옴
            if (Objects.equals(chatSession.getRecipientUserId(), -2)) {
//                System.out.println("GPT-4");
                String gptResponse = chatGptService.sendGpt(chatSession.getContent());
//                System.out.println("GPT-4 response: " + gptResponse);
                chatService.saveMessages(currentUserId, chatSession.getRecipientUserId(), chatSession.getContent(),
                        sessionId);
//                System.out.println("Saved message");
                chatService.saveMessages(chatSession.getRecipientUserId(), currentUserId, gptResponse, sessionId);
//                System.out.println("Saved GPT-4 response");
                return ResponseEntity.ok(sessionId);
            } else {
//                System.out.println("Normal message");
                if (!Objects.equals(chatSession.getContent(), "Initiating chat"))
                    chatService.saveMessages(currentUserId, chatSession.getRecipientUserId(), chatSession.getContent(),
                            sessionId);
                return ResponseEntity.ok(sessionId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending message: " + e.getMessage());
        }
    }

    @GetMapping("/user/chat/{sessionId}")
    public String chatPage(@PathVariable("sessionId") String sessionId, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String currentUserEmail = principal.getName();
        int currentUserId = userService.findUserIDByEmail(currentUserEmail);
        String currentUserProfileImg = userService.findUserProfileImageById(currentUserId);
        List<ChatUsersDto> followings = userService.getFollowings(currentUserId);
        List<ChatMessageDto> chatSessions = chatService.getFullChatHistoryForUser(currentUserId);

        int recipientUserId = chatService.getRecipientUserIdBySessionId(sessionId, currentUserId);
        String recipientUsername = userService.findUsernameById(recipientUserId);
        String recipientUserProfileImg = userService.findUserProfileImageById(recipientUserId);
        List<ChatUsersDto> sessionParticipants = userService.getSessionParticipants(chatSessions);

        model.addAttribute("email", currentUserEmail);
        model.addAttribute("userID", currentUserId);
        model.addAttribute("currentUserProfileImage", currentUserProfileImg);
        model.addAttribute("recipientUserId", recipientUserId);
        model.addAttribute("recipientUsername", recipientUsername);
        model.addAttribute("recipientUserProfileImage", recipientUserProfileImg);
        model.addAttribute("followings", followings);
        model.addAttribute("chatSessions", chatSessions);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("sessionParticipants", sessionParticipants);
        return "user/chat";
    }

    @GetMapping("/user/chat")
    public String chatPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String currentUserEmail = principal.getName();
        int currentUserId = userService.findUserIDByEmail(currentUserEmail);
        String currentUserProfileImg = userService.findUserProfileImageById(currentUserId);
        List<ChatUsersDto> followings = userService.getFollowings(currentUserId);
        List<ChatMessageDto> chatSessions = chatService.getFullChatHistoryForUser(currentUserId);

        // Gotta add all the attributes regardless of the following status of the user in the chat history
        List<ChatUsersDto> sessionParticipants = userService.getSessionParticipants(chatSessions);

        model.addAttribute("email", currentUserEmail);
        model.addAttribute("userID", currentUserId);
        model.addAttribute("currentUserProfileImage", currentUserProfileImg);
        model.addAttribute("followings", followings);
        model.addAttribute("chatSessions", chatSessions);
        model.addAttribute("sessionParticipants", sessionParticipants);
        model.addAttribute("motd", "팔로워에게 비공개 메세지를 보내보세요");

        return "user/chat";
    }

    @GetMapping("/user/leave/{sessionId}")
    public String leaveChat(@PathVariable("sessionId") String sessionId, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Integer currentUserId = userService.findUserIDByEmail(principal.getName());
        chatService.leaveChatSession(sessionId, currentUserId);
        return "redirect:/user/chat";
    }
}
