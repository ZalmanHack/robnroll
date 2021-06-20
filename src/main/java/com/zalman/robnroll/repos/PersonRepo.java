package com.zalman.robnroll.repos;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person, Long> {
    Person findByUsername(String username);
    Iterable<Person> findByBrigade(Brigade brigade);
}
