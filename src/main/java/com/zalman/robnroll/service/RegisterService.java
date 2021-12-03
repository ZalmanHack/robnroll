package com.zalman.robnroll.service;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Register;
import com.zalman.robnroll.repos.RegisterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private RegisterRepo registerRepo;

    public void addRecord(Register register) {
        registerRepo.save(register);
        System.out.println(registerRepo.findAll());
    }
}
