package com.zalman.robnroll.contoller;

import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Role;
import com.zalman.robnroll.repos.BrigadeRepo;
import com.zalman.robnroll.repos.PersonRepo;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private PersonRepo personRepo;



    @Autowired
    private BrigadeRepo brigadeRepo;



    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addPerson(Person person, Map<String, Object> model) {
        Person personFromDB = personRepo.findByUsername(person.getUsername());
        if(personFromDB != null) {
            model.put("message", "Person exist!");
            return "registration";
        }
        person.setActive(true);
        person.setRoles(Collections.singleton(Role.ADMIN));
        //person.setBrigade(brigadeRepo.findByName("qwerty"));
        personRepo.save(person);
        return "redirect:/login";
    }
}
