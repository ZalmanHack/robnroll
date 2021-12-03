package com.zalman.robnroll.util;

import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.repos.PersonRepo;
import com.zalman.robnroll.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    @Autowired
    PersonRepo personRepo;

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        Optional<Person> personDB = personRepo.findFirstByUsername(person.getUsername());
        if(!person.getPassword_1().isEmpty() && person.getPassword_2().isEmpty() && personDB.isPresent() && person.getUsername().equals(personDB.get().getUsername())) {
            errors.rejectValue("username", "", "Такое имя пользователя уже существует");
        }
        if(person.getPassword_1() != null && !person.getPassword_1().equals(person.getPassword_2())) {
            errors.rejectValue("password_1", "", "Пароли не равны!");
            errors.rejectValue("password_2", "", "Пароли не равны!");
        }
    }
}
