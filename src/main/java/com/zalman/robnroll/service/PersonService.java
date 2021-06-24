package com.zalman.robnroll.service;

import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Role;
import com.zalman.robnroll.repos.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class PersonService implements UserDetailsService {
    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${host.name}")
    private String hostName;

    @Override
    public UserDetails loadUserByUsername(String personName) throws UsernameNotFoundException {
        return personRepo.findByUsername(personName);
    }

    public boolean addPerson(Person person) {
        Person personFromDB = personRepo.findByUsername(person.getUsername());
        if (personFromDB != null) {
            return false;
        }
        person.setActive(false);
        person.setRoles(Collections.singleton(Role.USER));
        person.setActivationCode(UUID.randomUUID().toString());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepo.save(person);
        if (!person.getEmail().isEmpty()) {
            String message = String.format(
                    "<html lang=\"ru\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "Привет, %s!\n" +
                            "Добро пожаловать в Rob&Rolle!\n" +
                            "Пожалуйста, для активации аккаунта, перейдите по <a href=\"http://%s/activate/%s\">ссылке</a>." +
                            "</body>\n" +
                            "</html>",
                    person.getUsername(), hostName, person.getActivationCode());
            emailSender.send(person.getEmail(), "Код активации", message);
        }
        return true;
    }

    public boolean activatedPerson(String code) {
        Person person = personRepo.findByActivationCode(code);
        if (person == null) {
            return false;
        }
        person.setActive(true);
        person.setActivationCode(null);
        personRepo.save(person);
        return true;
    }

}
