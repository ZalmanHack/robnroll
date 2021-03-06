package com.zalman.robnroll.service;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Role;
import com.zalman.robnroll.repos.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    @Value("${upload.path}")
    private String upload_path;

    @Override
    public UserDetails loadUserByUsername(String personName) throws UsernameNotFoundException {
        Optional<Person> person = personRepo.findFirstByUsername(personName);
        return person.orElse(null);
    }

    public boolean addPerson(Person person) {
        Optional<Person> personFromDB = personRepo.findFirstByUsername(person.getUsername());
        if (personFromDB.isPresent()) {
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
                            "????????????, %s!\n" +
                            "?????????? ???????????????????? ?? Rob&Rolle!\n" +
                            "????????????????????, ?????? ?????????????????? ????????????????, ?????????????????? ???? <a href=\"https://%s/activate/%s\">????????????</a>." +
                            "</body>\n" +
                            "</html>",
                    person.getUsername(), hostName, person.getActivationCode());
            emailSender.send(person.getEmail(), "?????? ??????????????????", message);
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

    public void update(final Person person, MultipartFile raw_profile_pic, final Person authPerson, @Nullable Iterable<Role> checkedRoles) throws IOException {
        if (raw_profile_pic != null &&
                raw_profile_pic.getOriginalFilename() != null &&
                !raw_profile_pic.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(upload_path);
            if(!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + raw_profile_pic.getOriginalFilename();
            // ?????????????????? ????????
            raw_profile_pic.transferTo(new File(upload_path + '/' + resultFileName));
            person.setProfile_pic(resultFileName);
        }

        if(authPerson.isAdmin())
        {
            person.getRoles().clear();
            if(checkedRoles != null) {
                for (Role role : checkedRoles) {
                    person.getRoles().add(role);
                }
            }
        } else {
            person.setRoles(authPerson.getRoles());
            person.setBrigade(authPerson.getBrigade());
        }

        if(person.getId().equals(authPerson.getId()) || authPerson.isAdmin()) {
            if(!person.getPassword_1().isEmpty() && person.getPassword_1().equals(person.getPassword_2())) {
                person.setPassword(passwordEncoder.encode(person.getPassword_1()));
            }
        }
        person.setPassword_1("");
        person.setPassword_2("");
        personRepo.save(person);
    }

    public void delete(Person person) {
        person.getRoles().clear();
        personRepo.delete(person);
    }

    public Iterable<Person> brigadePersons(Person person) {
        return brigadePersons(person, "");
    }

    public Iterable<Person> brigadePersons(Person person, String email) {
        return personRepo.findByBrigadeAndEmailLike(person.getBrigade(), '%' + email + '%');
    }


    public Iterable<Person> allPersons() {
        return personRepo.findAll();
    }
}
