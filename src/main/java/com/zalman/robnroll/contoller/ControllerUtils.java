package com.zalman.robnroll.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class ControllerUtils {
    static Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<String, String>();
        for(FieldError object : bindingResult.getFieldErrors()) {
            errors.put(object.getField() + "Error", object.getDefaultMessage());
        }
        return errors;
    }
}