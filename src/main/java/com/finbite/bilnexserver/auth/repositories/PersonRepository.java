package com.finbite.bilnexserver.auth.repositories;

import com.finbite.bilnexserver.auth.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle Person related data queries
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, UUID>, JpaRepository<Person, UUID> {
    Optional<Person> findByEmail(String email);
}
