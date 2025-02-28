package com.finbite.bilnexserver.auth;

import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Person;
import com.finbite.bilnexserver.auth.models.SystemLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service to handle Person related operations
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public interface PersonService {
    /**
     * To create a new person
     *
     * @param person Person
     * @return Person
     */
    Person createPerson(Person person);

    /**
     * To find a Person by ID
     *
     * @param id ID of a Person
     * @return Person
     */
    Person findPersonById(UUID id) throws PersonNotFoundException;

    /**
     * To find person by Email
     *
     * @param email Email of a person
     * @return Page of person
     */
    Person findPersonByEmail(String email) throws PersonNotFoundException;

    /**
     * To find active person by email
     *
     * @param email Email of a person
     * @return Page of person
     */
    Person findActivePersonByEmail(String email) throws PersonNotFoundException;

    /**
     * To find all persons
     *
     * @param pageable Pageable of Persons
     * @return page of person
     */
    Page<Person> findAllPersons(Pageable pageable);

    /**
     * To update person with password
     *
     * @param person Person
     * @return Person
     */
    Person updatePersonWithPassword(Person person) throws PersonNotFoundException;

    /**
     * To delete a person by ID
     *
     * @param id Person ID
     */
    void deletePersonById(UUID id) throws PersonNotFoundException;

    /**
     * To restore a person by ID
     *
     * @param id Person ID
     */
    void restorePersonById(UUID id) throws PersonNotFoundException;

    /**
     * To update system language for the person
     *
     * @param id Person ID
     * @param language System Language
     */
    void updateSystemLanguage(UUID id, SystemLanguage language) throws PersonNotFoundException;
}

