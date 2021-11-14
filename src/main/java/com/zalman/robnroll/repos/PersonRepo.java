package com.zalman.robnroll.repos;

import com.zalman.robnroll.domain.Brigade;
import com.zalman.robnroll.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepo extends JpaRepository<Person, Long> {
    Person findByUsername(String username);
    Iterable<Person> findByBrigade(Brigade brigade);
    Iterable<Person> findByBrigadeNotNull();
    Iterable<Person> findByBrigadeIsNull();
    void removeById(Long id);
    Person findByEmail(String email);
    Person findByActivationCode(String code);
    Iterable<Person> findByBrigadeAndEmailLike(Brigade brigade, String email);

    @Modifying
    @Query("update Person p set p.activationCode = :actc where p.id = :person_id")
    void updateActivateCode(@Param("person_id") Long person_id, @Param("actc") String actc);
}
