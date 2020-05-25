package com.htecgroup.flightadvisor.repository;

import com.htecgroup.flightadvisor.domain.ApplicationUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<ApplicationUser> findByUsername(String username);

    @EntityGraph(attributePaths = "authorities")
    Optional<ApplicationUser> findOneWithAuthoritiesByUsername(String username);

    @EntityGraph(attributePaths = "authorities")
    @Nonnull
    List<ApplicationUser> findAll();
}
