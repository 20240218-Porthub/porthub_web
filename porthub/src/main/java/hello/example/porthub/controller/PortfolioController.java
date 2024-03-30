package hello.example.porthub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/ports")
public class PortfolioController {

    @GetMapping("/create")
    public String create() {
        return "portfolio/create";
    }

}
