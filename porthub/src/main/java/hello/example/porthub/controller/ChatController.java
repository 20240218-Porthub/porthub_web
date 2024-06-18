package hello.example.porthub.controller;

import hello.example.porthub.config.util.ChatSessionUtil;
import hello.example.porthub.domain.ChatMessageDto;
import hello.example.porthub.domain.ChatUsersDto;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.service.ChatService;
import hello.example.porthub.service.SessionParticipantService;
import hello.example.porthub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SessionParticipantService sessionParticipantService;

    private final MemberRepository memberRepository;
    @Autowired
    public ChatController(ChatService chatService, UserService userService,
                          SessionParticipantService sessionParticipantService, MemberRepository memberRepository) {
        this.chatService = chatService;
        this.userService = userService;
        this.sessionParticipantService = sessionParticipantService;
        this.memberRepository = memberRepository;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessage) {
        Integer senderId = chatMessage.getSenderUserId();
        Integer recipientId = chatMessage.getRecipientUserId();
        String content = chatMessage.getContent();
        String sessionId = chatMessage.getSessionId();
        chatService.saveMessages(senderId, recipientId, content, sessionId);
        return chatMessage;
    }

    @GetMapping("/api/chat-messages/{sessionId}")
    @ResponseBody
    public ResponseEntity<Object> getChatMessagesBySessionId(@PathVariable String sessionId, Principal principal) {
        System.out.println("sessionId: " + sessionId);
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
            System.out.println(chatSession);
            String currentUserEmail = principal.getName();

            int currentUserId = userService.findUserIDByEmail(currentUserEmail);
            String sessionId = ChatSessionUtil.generateSessionKey(currentUserId, chatSession.getRecipientUserId());
            sessionParticipantService.addParticipantToSession(sessionId, currentUserId);
            sessionParticipantService.addParticipantToSession(sessionId, chatSession.getRecipientUserId());
            chatService.saveMessages(currentUserId, chatSession.getRecipientUserId(), chatSession.getContent(),
                    sessionId);
            return ResponseEntity.ok(sessionId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending message: " + e.getMessage());
        }
    }

    @GetMapping("/user/chat/{sessionId}")
    public String chatPage(@PathVariable("sessionId") String sessionId, Model model, Principal principal) {
        if (principal == null) { return "redirect:/login"; }

        String currentUserEmail = principal.getName();
        int currentUserId = userService.findUserIDByEmail(currentUserEmail);
        String currentUserProfileImg = userService.findUserProfileImageById(currentUserId);
        List<ChatUsersDto> followings = userService.getFollowings(currentUserId);
        List<ChatMessageDto> chatSessions = chatService.getFullChatHistoryForUser(currentUserId);

        int recipientUserId = chatService.getRecipientUserIdBySessionId(sessionId, currentUserId);
        String recipientUsername = userService.findUsernameById(recipientUserId);
        String recipientUserProfileImg = userService.findUserProfileImageById(recipientUserId);

        model.addAttribute("email", currentUserEmail);
        model.addAttribute("userID", currentUserId);
        model.addAttribute("currentUserProfileImage", currentUserProfileImg);
        model.addAttribute("recipientUserId", recipientUserId);
        model.addAttribute("recipientUsername", recipientUsername);
        model.addAttribute("recipientUserProfileImage", recipientUserProfileImg);
        model.addAttribute("followings", followings);
        model.addAttribute("chatSessions", chatSessions);
        model.addAttribute("sessionId", sessionId);
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

        List<MemberDto> followingUserInfos = new ArrayList<>();
        for (ChatUsersDto following : followings) {
            MemberDto followingUserInfo = memberRepository.findmemberByUserID(following.getId());
            followingUserInfos.add(followingUserInfo);
        }

        model.addAttribute("email", currentUserEmail);
        model.addAttribute("userID", currentUserId);
        model.addAttribute("currentUserProfileImage", currentUserProfileImg);
        model.addAttribute("followings", followings);
        model.addAttribute("chatSessions", chatSessions);
        model.addAttribute("followingUserInfos", followingUserInfos);
        model.addAttribute("motd", "팔로워에게 비공개 메세지를 보내보세요");

        return "user/chat";
    }

    @GetMapping("/user/leave/{sessionId}")
    public String leaveChat(@PathVariable("sessionId") String sessionId, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Integer currentUserId = userService.findUserIDByEmail(principal.getName());
        System.out.println(currentUserId);
        chatService.leaveChatSession(sessionId, currentUserId);
        return "redirect:/user/chat";
    }
}
