package com.finbite.bilnexserver.auth.implementations;

import com.finbite.bilnexserver.auth.events.EmailVerificationProducerService;
import com.finbite.bilnexserver.auth.PersonService;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Person;
import com.finbite.bilnexserver.auth.models.Role;
import com.finbite.bilnexserver.auth.models.SystemLanguage;
import com.finbite.bilnexserver.auth.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementations of PersonService
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EmailVerificationProducerService emailVerificationProducerService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Person createPerson(Person person) {
        if (person.getRole() == null) {
            person.setRole(Role.USER);
        }

        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        Person savedPerson = personRepository.saveAndFlush(person);

        emailVerificationProducerService.sendVerificationRequest(person.getEmail());

        return savedPerson;
    }

    @Override
    public Person findPersonById(UUID id) throws PersonNotFoundException {
        Optional<Person> person = personRepository.findById(id);

        if (person.isEmpty()) {
            throw new PersonNotFoundException(id);
        }

        return person.get();
    }

    @Override
    public Person findPersonByEmail(String email) throws PersonNotFoundException {
        Optional<Person> person = personRepository.findByEmail(email);

        if (person.isEmpty()) {
            throw new PersonNotFoundException(email);
        }

        return person.get();
    }

    @Override
    public Person findActivePersonByEmail(String email) throws PersonNotFoundException {
        Person person = findPersonByEmail(email);

        if(!person.isActive()) {
            throw new PersonNotFoundException(email);
        }

        return person;
    }

    @Override
    public Page<Person> findAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Override
    public Person updatePersonWithPassword(Person person) throws PersonNotFoundException {
        if (findPersonById(person.getId()) != null) {
            person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
            return personRepository.saveAndFlush(person);
        }

        return null;
    }

    @Override
    public void deletePersonById(UUID id) throws PersonNotFoundException {
        Person person = findPersonById(id);
        person.setActive(false);
        personRepository.saveAndFlush(person);
    }

    @Override
    public void restorePersonById(UUID id) throws PersonNotFoundException {
        Person person = findPersonById(id);
        person.setActive(true);
        personRepository.saveAndFlush(person);
    }

    @Override
    public void updateSystemLanguage(UUID id, SystemLanguage language) throws PersonNotFoundException {
        Person person = findPersonById(id);
        person.setDefaultSystemLanguage(language);
        personRepository.saveAndFlush(person);
    }
}
