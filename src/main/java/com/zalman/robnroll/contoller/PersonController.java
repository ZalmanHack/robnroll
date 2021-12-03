package com.zalman.robnroll.contoller;

import com.zalman.robnroll.domain.Event;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Register;
import com.zalman.robnroll.domain.Role;
import com.zalman.robnroll.repos.BrigadeRepo;
import com.zalman.robnroll.repos.PersonRepo;
import com.zalman.robnroll.service.PersonService;
import com.zalman.robnroll.service.RegisterService;
import com.zalman.robnroll.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.time.LocalDateTime;
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
    private RegisterService registerService;


    @Autowired
    private BrigadeRepo brigadeRepo;

    @Autowired
    private PersonValidator personValidator;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String personList(@RequestParam(name = "activeCategory", required = false, defaultValue = "Все пользователи") String activeCategory,
                             @RequestParam(name = "filter_name", required = false, defaultValue = "") String filter_name,
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
    @PreAuthorize("hasAuthority('ADMIN') || " +
            "#auth_person.id.equals(#person.id) || " +
            "(#auth_person.brigade != null && #person.brigade != null && #auth_person.brigade.id.equals(#person.brigade.id))")
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

//        Register register = new Register();
//        register.setDateTime(LocalDateTime.now());
//        register.setEvent(Event.MESSAGE);
//        register.setPerson(person);
//        register.setMessage("Создать робота");
//        registerService.addRecord(register);


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

        personValidator.validate(person, bindingResult);
        model.addAttribute("person", person);
        model.addAttribute("roles", Role.values());
        model.addAttribute("brigades", brigadeRepo.findAll());

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "personEdit";
        }

        ArrayList<Role> checkedRoles = new ArrayList<>();
        if(auth_person.isAdmin()) {
            Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
            for (String key : form.keySet()) {
                if (roles.contains(key)) {
                    checkedRoles.add(Role.valueOf(key));
                }
            }
        }
        personService.update(person, raw_profile_pic, auth_person, checkedRoles);
        return "redirect:/person/{person}";
    }

    @PreAuthorize("#auth_person.id.equals(#person.id) || hasAuthority('ADMIN')")
    @DeleteMapping(value = "{person}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deletePerson(@AuthenticationPrincipal Person auth_person, @PathVariable Person person) {
        personService.delete(person);
        return ResponseEntity.ok().build();
    }



//    @PreAuthorize("#auth_person.id.equals(#person.id) || hasAuthority('ADMIN')")
//    @PostMapping(value = "{person}/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> getPersonStat(@AuthenticationPrincipal Person auth_person, @PathVariable Person person) {
//        personService.getStatistic(person);
//        return ResponseEntity.ok().build();
//    }

}
