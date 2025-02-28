package com.finbite.bilnexserver.auth;

import com.finbite.bilnexserver.auth.exceptions.CompanyNotFoundException;
import com.finbite.bilnexserver.auth.models.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service to handle Company related operations
 * 
 * @author vinodjohn
 * @created 28.02.2025
 */
public interface CompanyService {
    /**
     * To create a new company
     *
     * @param company Company
     * @return Company
     */
    Company createCompany(Company company);

    /**
     * To find a Company by ID
     *
     * @param id ID of a Company
     * @return Company
     */
    Company findCompanyById(UUID id) throws CompanyNotFoundException;

    /**
     * To find all companies
     *
     * @param pageable Pageable of Companies
     * @return page of company
     */
    Page<Company> findAllCompanies(Pageable pageable);

    /**
     * To update company
     *
     * @param company Company
     * @return Company
     */
    Company updateCompany(Company company) throws CompanyNotFoundException;

    /**
     * To delete a company by ID
     *
     * @param id Company ID
     */
    void deleteCompanyById(UUID id) throws CompanyNotFoundException;

    /**
     * To restore a company by ID
     *
     * @param id Company ID
     */
    void restoreCompanyById(UUID id) throws CompanyNotFoundException;
}
