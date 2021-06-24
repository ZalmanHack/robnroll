package com.zalman.robnroll.contoller;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Role;
import com.zalman.robnroll.repos.BrigadeRepo;
import com.zalman.robnroll.repos.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private BrigadeRepo brigadeRepo;

    @Value("${upload.path}")
    private String upload_path;

    @GetMapping
    public String personList(@RequestParam(name = "activeCategory", required = false, defaultValue = "Все") String activeCategory,
                             Model model) {
        Iterable<Person> persons;
        String[] categories = new String[] {"Все", "В бригаде", "Без бригады"};
        switch (activeCategory) {
            case "В бригаде": persons = personRepo.findByBrigadeNotNull(); break;
            case "Без бригады": persons = personRepo.findByBrigadeIsNull(); break;
            default: persons = personRepo.findAll();
                activeCategory = "Все";
        }
        model.addAttribute("activeCategory", activeCategory);
        model.addAttribute("categories", categories);
        model.addAttribute("persons", persons);
        return "personList";
    }

    @GetMapping("{person}/edit")
    public String personEdit(@PathVariable Person person, Model model) {
        model.addAttribute("person", person);
        model.addAttribute("roles", Role.values());
        model.addAttribute("brigades", brigadeRepo.findAll());
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

    @PostMapping("{person}/delete")
    public String deletePerson(@PathVariable Person person) {
        if(person != null && personRepo.existsById(person.getId())) {
            person.getRoles().clear();
            personRepo.delete(person);
        }
        return "redirect:/person";
    }
}
