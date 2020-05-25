package com.htecgroup.flightadvisor.repository;

import com.htecgroup.flightadvisor.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
