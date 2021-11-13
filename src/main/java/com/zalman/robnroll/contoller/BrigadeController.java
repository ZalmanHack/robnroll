package com.zalman.robnroll.contoller;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Role;
import com.zalman.robnroll.repos.BrigadeRepo;
import com.zalman.robnroll.repos.PersonRepo;
import com.zalman.robnroll.service.BrigadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller

@RequestMapping("/brigade")
public class BrigadeController {
    @Autowired
    private BrigadeRepo brigadeRepo;

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    BrigadeService brigadeService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String main(@RequestParam(name = "activeCategory", required = false, defaultValue = "Все бригады") String activeCategory,
                       @RequestParam(name = "filter_name", required = false, defaultValue = "") String filter_name,
                       Model model) {


        List<String> categories = new ArrayList<>();
        categories.add("Все бригады");

        if (activeCategory.isEmpty() || activeCategory.equals("Все бригады") || brigadeRepo.findByName(activeCategory) == null) {
            Iterable<Brigade> brigades = brigadeRepo.findByNameLike('%' + filter_name + '%');
            activeCategory = "Все бригады";
            model.addAttribute("brigades", brigades);
        } else if (brigadeRepo.findByName(activeCategory) != null) {
            categories.add(activeCategory); // добавляем потому что этой категории в списке изначально не было, ее сгенерировала страница клиента
            Iterable<Person> persons = personRepo.findByBrigadeAndEmailLike(brigadeRepo.findByName(activeCategory), '%' + filter_name + '%');
            model.addAttribute("persons", persons);
        }

        model.addAttribute("activeCategory", activeCategory);
        model.addAttribute("categories", categories);
        model.addAttribute("filter_name", filter_name);
        return "brigadeList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{brigade}/edit")
    public String personEdit(
            @PathVariable Brigade brigade, Model model) {
        return "comingSoon";
    }

    //TODO Падает при работае с пустой бригадой

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String addBrigade(@Valid Brigade brigade,
                             BindingResult bindingResult,
                             Model model) {

        model.addAttribute("activeCategory", "Все бригады");
        model.addAttribute("categories", new String[]{"Все бригады"});
        model.addAttribute("filter_name", "");

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("brigades", brigadeRepo.findAll());
            return "brigadeList";
        }

        if (!brigadeService.addBrigade(brigade)) {
            model.addAttribute("nameError", "Такая бригада уже существует!");
        }
        model.addAttribute("brigades", brigadeRepo.findAll());

        return "brigadeList";
    }

    //TODO Разобраться с spring security.
    // После изменения бригады не обновляются данные пользователя сессии.
    // Нужно сделать обновление авторизации. Пример:
    // https://stackoverflow.com/questions/9910252/how-to-reload-authorities-on-user-update-with-spring-security

    @PreAuthorize("#person.brigade.id == #brigadeId || hasAuthority('ADMIN')")
    @GetMapping("{brigadeId}")
    public String showBrigade(@AuthenticationPrincipal Person person,
                              @PathVariable(name = "brigadeId") long brigadeId,
                              Model model) {
        return "comingSoon";
    }
}
