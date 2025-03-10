package com.finbite.bilnexserver.auth.configs;

import com.finbite.bilnexserver.auth.implementations.UserDetailsServiceImpl;
import com.finbite.bilnexserver.auth.utils.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Authentication Token Filter implementation
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Slf4j
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if (jwt != null) {
                assert securityUtils != null;
                if (securityUtils.validateJwtToken(jwt)) {
                    String username = securityUtils.getUserNameFromJwtToken(jwt);

                    assert userDetailsService != null;
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // PRIVATE METHODS //
    private String parseJwt(HttpServletRequest request) {
        assert securityUtils != null;
        return securityUtils.getJwtFromCookies(request);
    }
}
