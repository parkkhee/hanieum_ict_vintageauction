package hello.hellospring.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    // 기본 페이지 home.html로 설정
    @GetMapping("/")
    public String home() {
        return "home";
    }


}