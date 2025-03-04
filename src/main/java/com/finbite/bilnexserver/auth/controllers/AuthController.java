package com.finbite.bilnexserver.auth.controllers;

import com.finbite.bilnexserver.auth.AuthService;
import com.finbite.bilnexserver.auth.CompanyService;
import com.finbite.bilnexserver.auth.PersonService;
import com.finbite.bilnexserver.auth.TokenRefreshService;
import com.finbite.bilnexserver.auth.configs.CustomUserDetails;
import com.finbite.bilnexserver.auth.dtos.SignIn;
import com.finbite.bilnexserver.auth.dtos.SignUp;
import com.finbite.bilnexserver.auth.events.EmailVerificationPublisher;
import com.finbite.bilnexserver.auth.exceptions.CompanyNotFoundException;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Company;
import com.finbite.bilnexserver.auth.models.Person;
import com.finbite.bilnexserver.auth.models.TokenRefresh;
import com.finbite.bilnexserver.auth.utils.AuthUtils;
import com.finbite.bilnexserver.auth.utils.SecurityUtils;
import com.finbite.bilnexserver.common.exceptions.EmailVerificationException;
import com.finbite.bilnexserver.common.exceptions.EmailVerificationNotFoundException;
import com.finbite.bilnexserver.notification.EmailVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Controller to handle security related requests
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final SecurityUtils securityUtils;

    private final PersonService personService;

    private final CompanyService companyService;

    private final AuthService authService;

    private final TokenRefreshService tokenRefreshService;

    private final EmailVerificationPublisher emailVerificationPublisher;

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignIn signIn) throws PersonNotFoundException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signIn.email(),
                        signIn.password().equals("!") ? authService.getGooglePassword() : signIn.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        ResponseCookie generatedJwtCookie = securityUtils.generateJwtCookie(customUserDetails.getUsername());
        TokenRefresh tokenRefresh = tokenRefreshService.createRefreshToken(customUserDetails.getUserDto().id());
        ResponseCookie refreshJwtCookie = securityUtils.generateRefreshJwtCookie(tokenRefresh.getToken());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, generatedJwtCookie.toString());
        httpHeaders.add(HttpHeaders.SET_COOKIE, refreshJwtCookie.toString());
        Person person = personService.findPersonById(customUserDetails.getUserDto().id());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(person.toPersonDto());
    }

    //Step-1
    @PostMapping("/sign-up")
    public ResponseEntity<?> startSignup(@Valid @RequestBody SignUp signUp) {
        emailVerificationPublisher.sendVerificationRequest(signUp.email());
        return ResponseEntity.ok().body(signUp);
    }

    //Step-2
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody SignUp signUp) throws EmailVerificationNotFoundException,
            EmailVerificationException {
        emailVerificationService.verifyCode(signUp.email(), signUp.code());
        return ResponseEntity.ok().body(signUp);
    }

    //Step-3 and 4
    @PostMapping("/sign-up-confirm")
    public ResponseEntity<?> signUpCompany(@Valid @RequestBody SignUp signUp) {
        if (!signUp.isVerified()) {
            throw new RuntimeException("Email is not verified! Please try again after verification!");
        }

        Company company = AuthUtils.translateSignupToCompany(signUp);

        try {
            company = companyService.findCompanyByRegCode(company.getRegCode());
        } catch (CompanyNotFoundException e) {
            company = companyService.createCompany(company);
        }

        Person person = new Person();
        person.setEmail(signUp.email());
        person.setPassword(signUp.password());
        person.setVerified(true);
        person.setCompanies(Collections.singletonList(company));

        if (signUp.code().equals("0")) {
            person.setPassword(authService.getGooglePassword());
            person.setGoogleUser(true);
        }

        Person createdPerson = personService.createPerson(person);

        return ResponseEntity.ok().body(createdPerson.toPersonDto());
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

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String idToken = payload.get("token");
        return ResponseEntity.ok(authService.handleGoogleLogin(idToken));
    }
}

