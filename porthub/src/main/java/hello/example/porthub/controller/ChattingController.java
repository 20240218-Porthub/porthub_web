package hello.example.porthub.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChattingController {
    
    @GetMapping("/user/chat")
    public String chatGet(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // userDetails 객체에서 현재 로그인한 사용자의 이름(Username)을 가져옴
        String username = userDetails.getUsername();

        // 모델에 사용자 이름을 추가: 이 이름은 뷰에서 사용됨
        model.addAttribute("name", username);
        return "chat";
    }
}
