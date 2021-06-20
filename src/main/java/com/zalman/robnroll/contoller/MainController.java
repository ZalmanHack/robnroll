package com.zalman.robnroll.contoller;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.repos.BrigadeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private BrigadeRepo brigadeRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        Iterable<Brigade> brigades = brigadeRepo.findAll();
        model.put("brigades", brigades);
        return "main";
    }

    @PostMapping("/main")
    public String addRole(
            @AuthenticationPrincipal Person person,
            @RequestParam(name="name", required=false, defaultValue="") String name,
            Map<String, Object> model) {
        Brigade brigade = new Brigade(name);
        System.out.println(person.getBrigade().getName());
        brigadeRepo.save(brigade);

        // ВРЕМЕННОЕ РЕШЕНИЕ
        Iterable<Brigade> brigades = brigadeRepo.findAll();
        model.put("brigades", brigades);
        return "main";
    }

    @PostMapping("/brigade_filter")
    public String searchRole(
            @RequestParam(name="name", required=false, defaultValue="") String name,
            Map<String, Object> model) {
        Iterable<Brigade> brigades;
        if(name != null && !name.isEmpty()) {
            brigades = brigadeRepo.findByNameLike('%' + name + '%');
        } else {
            brigades = brigadeRepo.findAll();
        }
        model.put("brigades", brigades);
        return "main";
    }
}