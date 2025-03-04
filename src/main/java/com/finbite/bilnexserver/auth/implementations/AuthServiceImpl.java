package com.finbite.bilnexserver.auth.implementations;

import com.finbite.bilnexserver.auth.AuthService;
import com.finbite.bilnexserver.auth.PersonService;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of Auth Service
 *
 * @author vinodjohn
 * @created 04.03.2025
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PersonService personService;
    @Value("bilnex.app.googlePassword")
    private String googlePassword;

    public Map<String, Object> handleGoogleLogin(String idToken) {
        String email = "";
        boolean isNewUser = false;

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            email = decodedToken.getEmail();
            personService.findPersonByEmail(email);
        } catch (PersonNotFoundException e) {
            isNewUser = true;
        } catch (Exception e) {
            throw new RuntimeException("Invalid Google Token");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("isNewUser", isNewUser);
        response.put("email", email);
        return response;
    }

    @Override
    public String getGooglePassword() {
        return googlePassword;
    }
}
