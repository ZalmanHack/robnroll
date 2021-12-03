package com.zalman.robnroll.repos;

import com.zalman.robnroll.domain.Brigade;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BrigadeRepo extends CrudRepository<Brigade, Long> {
    Iterable<Brigade> findByNameLike(String name);
    Optional<Brigade> findFirstByName(String name);
    Brigade findByName(String name);

}
