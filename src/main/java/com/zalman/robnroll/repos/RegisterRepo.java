package com.zalman.robnroll.repos;

import com.zalman.robnroll.domain.Person;
import com.zalman.robnroll.domain.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepo extends JpaRepository<Register, Long> {

}
