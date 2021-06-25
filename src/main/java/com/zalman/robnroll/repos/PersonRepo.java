package com.zalman.robnroll.repos;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person, Long> {
    Person findByUsername(String username);
    Iterable<Person> findByBrigade(Brigade brigade);
    Iterable<Person> findByBrigadeNotNull();
    Iterable<Person> findByBrigadeIsNull();
    void removeById(Long id);
    Person findByEmail(String email);
    Person findByActivationCode(String code);
    Iterable<Person> findByBrigadeAndEmailLike(Brigade brigade, String email);
}
