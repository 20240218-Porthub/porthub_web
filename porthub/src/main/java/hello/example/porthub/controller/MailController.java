package hello.example.porthub.controller;

import hello.example.porthub.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @ResponseBody
    @PostMapping("/Email")
    public String MailSend(@RequestParam("Email") String Email) {
        int number = mailService.sendMail(Email);
        String num = "" + number;
        return num;
    }

}
