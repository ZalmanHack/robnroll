package com.zalman.robnroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Brigade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

//    @OneToMany(mappedBy = "brigade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<Person> persons;

    public Brigade() {
    }

//    public Set<Person> getPersons() {
//        return persons;
//    }
//
//    public void setPersons(Set<Person> persons) {
//        this.persons = persons;
//    }

    public Brigade(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
