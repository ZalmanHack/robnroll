package com.zalman.robnroll.service;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.repos.BrigadeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrigadeService {
    @Autowired
    BrigadeRepo brigadeRepo;

    public void addBrigade(Brigade brigade) {
        brigadeRepo.save(brigade);
    }
}
