package com.zalman.robnroll.contoller;

import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.service.PersonService;
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

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addPerson(@Valid Person person,
                            BindingResult bindingResult,
                            Model model) {

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if(person.getPassword() != null && !person.getPassword().equals(person.getPassword_2())) {
            model.addAttribute("passwordError", "Пароли не равны!");
            return "registration";
        }

        if(person.getPassword() == null || person.getPassword().isEmpty()) {
            model.addAttribute("passwordError", "Данное поле не должно быть пустым");
            return "registration";
        }

        if(person.getPassword_2() == null || person.getPassword_2().isEmpty()) {
            model.addAttribute("password_2Error", "Данное поле не должно быть пустым");
            return "registration";
        }

        if(!personService.addPerson(person)) {
            model.addAttribute("usernameError", "Такой пользователь уже существует!");
            return "registration";
        }

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
