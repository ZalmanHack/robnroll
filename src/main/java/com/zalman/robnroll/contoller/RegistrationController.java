package com.zalman.robnroll.contoller;

import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.service.PersonService;
import com.zalman.robnroll.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private PersonService personService;

    @Autowired
    private PersonValidator personValidator;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addPerson(@Valid Person person,
                            BindingResult bindingResult,
                            Model model) {
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        personService.addPerson(person);
        model.addAttribute("info_message", String.format("%s, Вам на почту %s было отправлено письмо с ключём активации!", person.getUsername(), person.getEmail()));
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code,
                           Model model) {
        boolean isActivated = personService.activatedPerson(code);
        if(isActivated) {
            model.addAttribute("success_message", "Пользователь успешно активирован!");
        } else {
            model.addAttribute("error_message", "Код активации не действителен!");
        }
        return "login";
    }
}
