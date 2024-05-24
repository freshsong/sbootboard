package net.musecom.boot_board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")  
    public String index() {
        System.out.println("index파일 시작");
        return "index";
    }
}
