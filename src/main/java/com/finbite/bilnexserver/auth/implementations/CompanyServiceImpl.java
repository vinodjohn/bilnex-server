package com.finbite.bilnexserver.auth.implementations;

import com.finbite.bilnexserver.auth.CompanyService;
import com.finbite.bilnexserver.auth.exceptions.CompanyNotFoundException;
import com.finbite.bilnexserver.auth.models.Company;
import com.finbite.bilnexserver.auth.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of CompanyService
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company createCompany(Company company) {
        return companyRepository.saveAndFlush(company);
    }

    @Override
    public Company findCompanyById(UUID id) throws CompanyNotFoundException {
        Optional<Company> company = companyRepository.findById(id);

        if (company.isEmpty()) {
            throw new CompanyNotFoundException(id);
        }

        return company.get();
    }

    @Override
    public Page<Company> findAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    @Override
    public Company updateCompany(Company company) throws CompanyNotFoundException {
        if (findCompanyById(company.getId()) != null) {
            return companyRepository.saveAndFlush(company);
        }

        return null;
    }

    @Override
    public void deleteCompanyById(UUID id) throws CompanyNotFoundException {
        Company company = findCompanyById(id);
        company.setActive(false);
        companyRepository.saveAndFlush(company);
    }

    @Override
    public void restoreCompanyById(UUID id) throws CompanyNotFoundException {
        Company company = findCompanyById(id);
        company.setActive(true);
        companyRepository.saveAndFlush(company);
    }
}
