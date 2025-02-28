package com.finbite.bilnexserver.auth.controllers;

import com.finbite.bilnexserver.auth.PersonService;
import com.finbite.bilnexserver.auth.configs.CustomUserDetails;
import com.finbite.bilnexserver.auth.dtos.ChangePassword;
import com.finbite.bilnexserver.auth.dtos.ObjectListDto;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Person;
import com.finbite.bilnexserver.auth.models.SystemLanguage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.finbite.bilnexserver.common.utils.AppUtils.getSortOfColumn;
import static com.finbite.bilnexserver.common.utils.Constants.Data.DEFAULT_ITEMS_PER_PAGE;

/**
 * Controller to handle person related operations
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping
    public ResponseEntity<ObjectListDto> getSortedPersonByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) {
        Page<Person> personPage = personService.findAllPersons(PageRequest.of(pageNum, totalItem,
                getSortOfColumn(sort, order)));
        List<Person> personList = personPage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(personList, pageNum, personPage.getTotalElements());

        return ResponseEntity.ok(objectListDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable UUID id) throws PersonNotFoundException {
        Person person = personService.findPersonById(id);
        return ResponseEntity.ok(person);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword changePassword)
            throws PersonNotFoundException {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Person person = personService.findPersonById(customUserDetails.getUserDto().id());
        person.setPassword(changePassword.newPassword());
        personService.updatePersonWithPassword(person);
        return ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity<?> createPerson(@Valid @RequestBody Person person) {
        Person newPerson = personService.createPerson(person);
        return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable UUID id) throws PersonNotFoundException {
        personService.deletePersonById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restorePerson(@PathVariable UUID id) throws PersonNotFoundException {
        personService.restorePersonById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/update-system-language/{lang}")
    public ResponseEntity<?> updateSystemLanguage(@PathVariable UUID id, @PathVariable SystemLanguage lang) throws PersonNotFoundException {
        personService.updateSystemLanguage(id, lang);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

