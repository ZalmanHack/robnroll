package com.zalman.robnroll.contoller;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Role;
import com.zalman.robnroll.repos.BrigadeRepo;
import com.zalman.robnroll.repos.PersonRepo;
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
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private BrigadeRepo brigadeRepo;

    @Value("${upload.path}")
    private String upload_path;

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

        Iterable<Person> persons;
        if(person.getBrigade() != null) {
            persons = personRepo.findByBrigadeAndEmailLike(person.getBrigade(), '%' + filter_name + '%');
            model.addAttribute("persons", persons);
        }

        model.addAttribute("activeCategory", "Бригада");
        model.addAttribute("categories", new String[] {"Бригада"});
        model.addAttribute("filter_name", filter_name);
        return "person";
    }

    @PreAuthorize("#auth_person.id.equals(#person.id) || hasAuthority('ADMIN')")
    @GetMapping("{person}/edit")
    public String personEdit(
            @AuthenticationPrincipal Person auth_person,
            @PathVariable Person person,
            @RequestParam(name = "filter_name", required = false, defaultValue = "") String filter_name,
            Model model) {
        model.addAttribute("person", auth_person);
        model.addAttribute("roles", Role.values());
        model.addAttribute("brigades", brigadeRepo.findAll());

        Iterable<Person> persons;
        if(auth_person.getBrigade() != null) {
            persons = personRepo.findByBrigadeAndEmailLike(auth_person.getBrigade(), '%' + filter_name + '%');
            model.addAttribute("persons", persons);
        }

        model.addAttribute("activeCategory", "Бригада");
        model.addAttribute("categories", new String[] {"Бригада"});
        model.addAttribute("filter_name", filter_name);
        return "personEdit";
    }

    @PostMapping("{person}/save")
    public String personSave(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "brigadeId", required = false) Brigade brigade,
            @RequestParam(name = "profile_pic") MultipartFile profile_pic,
            @RequestParam Map<String, String> form,
            @PathVariable Person person) throws IOException {

        person.setUsername(username);
        person.setEmail(email);

        if (profile_pic != null &&
                profile_pic.getOriginalFilename() != null &&
                !profile_pic.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(upload_path);
            if(!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + profile_pic.getOriginalFilename();
            // загружаем файл
            profile_pic.transferTo(new File(upload_path + '/' + resultFileName));
            person.setProfile_pic(resultFileName);
        }

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        person.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                person.getRoles().add(Role.valueOf(key));
            }
        }
        person.setBrigade(brigade);
        personRepo.save(person);
        return "redirect:/person";
    }

//    @PreAuthorize("#auth_person.id.equals(#person.id) || hasAuthority('ADMIN')")
//    @PostMapping("{person}/save")
//    public String personSave(
//            @AuthenticationPrincipal Person auth_person,
////            @RequestParam(name = "profile_pic") MultipartFile profile_pic,
//            @RequestParam Map<String, String> form,
//            @Valid Person person,
//            BindingResult bindingResult,
//            Model model) throws IOException {
//
//        model.addAttribute("person", person);
//        model.addAttribute("activeCategory", "Бригада");
//        model.addAttribute("categories", new String[] {"Бригада"});
//
//        if(bindingResult.hasErrors()) {
//            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
//            model.mergeAttributes(errors);
//            return "personEdit";
//        }
//
//        if(person.getPassword() != null && !person.getPassword().equals(person.getPassword_2())) {
//            model.addAttribute("passwordError", "Пароли не равны!");
//            return "personEdit";
//        }
//
//        if(person.getPassword_2().isEmpty()) {
//            model.addAttribute("password_2Error", "Данное поле не должно быть пустым");
//            return "personEdit";
//        }
//
//        // работа с авой
////        if (profile_pic != null &&
////                profile_pic.getOriginalFilename() != null &&
////                !profile_pic.getOriginalFilename().isEmpty()) {
////            File uploadDir = new File(upload_path);
////            if(!uploadDir.exists()) {
////                uploadDir.mkdir();
////            }
////
////            String uuidFile = UUID.randomUUID().toString();
////            String resultFileName = uuidFile + "." + profile_pic.getOriginalFilename();
////            // загружаем файл
////            profile_pic.transferTo(new File(upload_path + '/' + resultFileName));
////            person.setProfile_pic(resultFileName);
////        }
//
//        // Работа с ролями
//        Set<String> roles = Arrays.stream(Role.values())
//                .map(Role::name)
//                .collect(Collectors.toSet());
//
//        person.getRoles().clear();
//        for (String key : form.keySet()) {
//            if (roles.contains(key)) {
//                person.getRoles().add(Role.valueOf(key));
//            }
//        }
//        personRepo.save(person);
//        return "person";
//    }

    @PostMapping("{person}/delete")
    public String deletePerson(@PathVariable Person person) {
        if(person != null && personRepo.existsById(person.getId())) {
            person.getRoles().clear();
            personRepo.delete(person);
        }
        return "redirect:/person";
    }
}
