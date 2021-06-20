package com.zalman.robnroll.service;

import com.zalman.robnroll.repos.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonService implements UserDetailsService {
    @Autowired
    private PersonRepo personRepo;
    @Override
    public UserDetails loadUserByUsername(String personName) throws UsernameNotFoundException {
        return personRepo.findByUsername(personName);
    }
}
