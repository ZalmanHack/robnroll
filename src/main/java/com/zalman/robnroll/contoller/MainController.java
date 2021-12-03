package com.zalman.robnroll.contoller;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Event;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Register;
import com.zalman.robnroll.repos.BrigadeRepo;
import com.zalman.robnroll.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private BrigadeRepo brigadeRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        System.out.println("main");


        return "greeting";
    }

    @GetMapping("/test")
    public String test(Map<String, Object> model) {
        System.out.println("Redirected");
        return "test";
    }

    @GetMapping("/test2")
    public String test2(Map<String, Object> model) {
        return "redirect:/test?success";
    }
}