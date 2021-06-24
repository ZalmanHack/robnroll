package com.zalman.robnroll.contoller;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.repos.BrigadeRepo;
import com.zalman.robnroll.repos.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/brigade")
public class BrigadeController {
    @Autowired
    private BrigadeRepo brigadeRepo;

    @GetMapping
    public String main(@RequestParam(required = false, defaultValue = "") String filter_name,
                       Model model) {
        Iterable<Brigade> brigades;
        if(filter_name != null && !filter_name.isEmpty()) {
            brigades = brigadeRepo.findByNameLike('%' + filter_name + '%');
        } else {
            brigades = brigadeRepo.findAll();
        }
        model.addAttribute("filter_name", filter_name);
        model.addAttribute("brigades", brigades);
        return "brigadeList";
    }

    @PostMapping
    public String addBrigade(
            /*@AuthenticationPrincipal Person person,*/
            @RequestParam(name="name", required=false, defaultValue="") String name,
            Model model) {
        Brigade brigade = new Brigade(name);
        brigadeRepo.save(brigade);

        // ВРЕМЕННОЕ РЕШЕНИЕ
        Iterable<Brigade> brigades = brigadeRepo.findAll();
        model.addAttribute("brigades", brigades);
        return "brigadeList";
    }
}
