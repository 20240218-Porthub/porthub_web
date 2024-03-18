package hello.example.porthub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    @GetMapping(value = {"/", "/main"})
    public String index() {
        return "portfolio/main";
    }


    @GetMapping(value = {"/login"})
    public String login() {
        return "register/login";
    }


    @GetMapping(value = {"/login2"})
    public String login2() {
        return "register/login2";
    }


    @GetMapping(value = {"/register"})
    public String register() {
        return "register/register";
    }


}

