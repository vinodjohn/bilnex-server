package com.finbite.bilnexserver.auth.repositories;

import com.finbite.bilnexserver.auth.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle Company related data queries
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company, UUID>, JpaRepository<Company, UUID> {
    Optional<Company> findByRegCode(String regCode);
}
