package com.finbite.bilnexserver.auth.implementations;

import com.finbite.bilnexserver.auth.PersonService;
import com.finbite.bilnexserver.auth.configs.CustomUserDetails;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserDetailsService
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PersonService personService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Person person = personService.findActivePersonByEmail(username);
            return new CustomUserDetails(person.toDto());
        } catch (PersonNotFoundException e) {
            throw new UsernameNotFoundException(e.getLocalizedMessage());
        }
    }
}
