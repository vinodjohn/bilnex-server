package com.finbite.bilnexserver.auth.controllers;

import com.finbite.bilnexserver.auth.CompanyService;
import com.finbite.bilnexserver.auth.PersonService;
import com.finbite.bilnexserver.auth.TokenRefreshService;
import com.finbite.bilnexserver.auth.configs.CustomUserDetails;
import com.finbite.bilnexserver.auth.dtos.PersonDto;
import com.finbite.bilnexserver.auth.dtos.SignIn;
import com.finbite.bilnexserver.auth.dtos.SignUp;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Company;
import com.finbite.bilnexserver.auth.models.Person;
import com.finbite.bilnexserver.auth.models.TokenRefresh;
import com.finbite.bilnexserver.auth.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

/**
 * Controller to handle security related requests
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PersonService personService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private TokenRefreshService tokenRefreshService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignIn signIn) throws PersonNotFoundException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signIn.email(), signIn.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        ResponseCookie generatedJwtCookie = securityUtils.generateJwtCookie(customUserDetails.getUsername());
        TokenRefresh tokenRefresh = tokenRefreshService.createRefreshToken(customUserDetails.getUserDto().id());
        ResponseCookie refreshJwtCookie = securityUtils.generateRefreshJwtCookie(tokenRefresh.getToken());

        String role = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No authorities found for person"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, generatedJwtCookie.toString());
        httpHeaders.add(HttpHeaders.SET_COOKIE, refreshJwtCookie.toString());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new PersonDto(customUserDetails.getUserDto().id(),
                        customUserDetails.getUserDto().email(), role));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUp signUp) throws PersonNotFoundException {
        //Step-1
        if (signUp.id() == null) {
            Person person = new Person();
            person.setEmail(signUp.email());

            personService.createPerson(person);
        }

        //Step-2
        if (signUp.isVerified() && signUp.company().id() == null) {
            Company company = new Company();
            company.setName(signUp.company().name());
            company.setRegCode(signUp.company().regCode());
            company.setVatNr(signUp.company().vatNr());
            company.setAddress(signUp.company().address());
            company.setCity(signUp.company().city());
            company.setZipcode(signUp.company().zipcode());

            company = companyService.createCompany(company);

            Person person = personService.findPersonById(signUp.id());
            person.setCompanies(Collections.singletonList(company));
            personService.updatePersonWithPassword(person);
        }

        //Step-3
        if (signUp.isVerified() && signUp.company().id() != null && signUp.password() != null) {
            Person person = personService.findPersonById(signUp.id());
            person.setPassword(signUp.password());
            personService.updatePersonWithPassword(person);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest) {
        String tokenRefreshRetrieved = securityUtils.getJwtRefreshFromCookies(httpServletRequest);

        if (tokenRefreshRetrieved != null && !tokenRefreshRetrieved.isBlank()) {
            TokenRefresh tokenRefresh = tokenRefreshService.findTokenRefreshByToken(tokenRefreshRetrieved);
            TokenRefresh tokenRefreshVerified = tokenRefreshService.verifyTokenExpiry(tokenRefresh);
            ResponseCookie refreshJwtCookie =
                    securityUtils.generateJwtCookie(tokenRefreshVerified.getPerson().getEmail());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshJwtCookie.getValue())
                    .build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut() {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID personId = customUserDetails.getUserDto().id();
        tokenRefreshService.deleteTokenRefreshByPersonId(personId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, securityUtils.getCleanJwtCookie().toString());
        httpHeaders.add(HttpHeaders.SET_COOKIE, securityUtils.getCleanJwtRefreshCookie().toString());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .build();
    }
}

