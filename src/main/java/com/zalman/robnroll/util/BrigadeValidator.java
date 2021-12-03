package com.zalman.robnroll.util;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.repos.BrigadeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BrigadeValidator implements Validator {

    @Autowired
    BrigadeRepo brigadeRepo;

    @Override
    public boolean supports(Class<?> aClass) {
        return Brigade.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Brigade brigade = (Brigade) o;
        if(brigadeRepo.findFirstByName(brigade.getName()).isPresent()) {
            errors.rejectValue("name", "", "Бригада с таким названием уже существует");
        }
    }
}
