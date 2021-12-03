package com.zalman.robnroll.restController;

import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonRestController {

    @Autowired
    PersonService personService;

    @GetMapping("all")
    public Iterable<Person> all() {
        return personService.allPersons();
    }

    @GetMapping("{person}/brigade")
    public Iterable<Person> brigade(@PathVariable Person person,
                                    @RequestParam(name = "filter_name", required = false, defaultValue = "") String filter_name)
    {
        System.out.println(filter_name);
        return personService.brigadePersons(person, filter_name);
    }

}
