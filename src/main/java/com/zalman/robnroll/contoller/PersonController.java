package com.zalman.robnroll.contoller;

import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Role;
import com.zalman.robnroll.repos.BrigadeRepo;
import com.zalman.robnroll.repos.PersonRepo;
import com.zalman.robnroll.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private PersonService personService;

    @Autowired
    private BrigadeRepo brigadeRepo;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String personList(@RequestParam(name = "activeCategory", required = false, defaultValue = "Все пользователи") String activeCategory,
                             Model model) {
        Iterable<Person> persons;
        String[] categories = new String[] {"Все пользователи", "В бригаде", "Без бригады"};
        switch (activeCategory) {
            case "В бригаде": persons = personRepo.findByBrigadeNotNull(); break;
            case "Без бригады": persons = personRepo.findByBrigadeIsNull(); break;
            default: persons = personRepo.findAll();
                activeCategory = "Все пользователи";
        }
        model.addAttribute("activeCategory", activeCategory);
        model.addAttribute("categories", categories);
        model.addAttribute("persons", persons);
        return "personList";
    }

    //@PreAuthorize("#auth_person.id.equals(#person.id) || hasAuthority('ADMIN')")
    @GetMapping("{person}")
    public String personShow (
            @AuthenticationPrincipal Person auth_person,
            @PathVariable Person person,
            @RequestParam(name = "filter_name", required = false, defaultValue = "") String filter_name,
            Model model) {
        model.addAttribute("person", person);
        model.addAttribute("roles", Role.values());
        model.addAttribute("brigades", brigadeRepo.findAll());

        Iterable<Person> persons = new ArrayList<>();
        if(person.getBrigade() != null) {
            persons = personRepo.findByBrigadeAndEmailLike(person.getBrigade(), '%' + filter_name + '%');
        }
        model.addAttribute("persons", persons);
        model.addAttribute("activeCategory", "Бригада");
        model.addAttribute("categories", new String[] {"Бригада"});
        model.addAttribute("filter_name", filter_name);
        return "personShow";
    }

    @PreAuthorize("#auth_person.id.equals(#person.id) || hasAuthority('ADMIN')")
    @GetMapping("{person}/edit")
    public String personEdit(
            @AuthenticationPrincipal Person auth_person,
            @PathVariable Person person,
            @RequestParam(name = "filter_name", required = false, defaultValue = "") String filter_name,
            Model model) {
        model.addAttribute("person", person);
        model.addAttribute("roles", Role.values());
        model.addAttribute("brigades", brigadeRepo.findAll());
        return "personEdit";
    }


    @PreAuthorize("#auth_person.id.equals(#person.id) || hasAuthority('ADMIN')")
    @PostMapping(value = "{person}/save", consumes = "multipart/form-data")
    public String personSave(
            @AuthenticationPrincipal Person auth_person,
            @RequestParam(name = "raw_profile_pic") MultipartFile raw_profile_pic,
            @RequestParam Map<String, String> form,
            @Valid Person person,
            BindingResult bindingResult, // если не принимать этот параметр, то не происходит обработка исключений, которые попдают сюда
            Model model) throws IOException {

        model.addAttribute("person", person);
        model.addAttribute("roles", Role.values());
        model.addAttribute("brigades", brigadeRepo.findAll());

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "personEdit";
        }

        if(person.getPassword_1() != null && !person.getPassword_1().equals(person.getPassword_2())) {
            model.addAttribute("password_1Error", "Пароли не равны!");
            return "personEdit";
        }

        if(!personService.checkUsername(person)) {
            model.addAttribute("usernameError", "Такой пользователь уже существует!");
            return "personEdit";
        }

        if(auth_person.isAdmin()) {
            Set<String> roles = Arrays.stream(Role.values())
                    .map(Role::name)
                    .collect(Collectors.toSet());

            ArrayList<Role> checkedRoles = new ArrayList<>();
            for (String key : form.keySet()) {
                if (roles.contains(key)) {
                    checkedRoles.add(Role.valueOf(key));
                }
            }
            personService.update(person, raw_profile_pic, auth_person, checkedRoles);
        } else {
            personService.update(person, raw_profile_pic, auth_person);
        }

        return "redirect:/person/{person}";
    }


    @PostMapping("{person}/delete")
    public String deletePerson(@PathVariable Person person) {
        if(person != null && personRepo.existsById(person.getId())) {
            person.getRoles().clear();
            personRepo.delete(person);
        }
        return "redirect:/person";
    }
}
