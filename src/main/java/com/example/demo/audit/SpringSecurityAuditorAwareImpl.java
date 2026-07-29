package com.example.demo.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpringSecurityAuditorAwareImpl implements AuditorAware<User> {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        /*
         * ----- Note -------------
         *   No logged-in user:
         * - customer registration
         * - application startup
         * - scheduled jobs
         * - background processes
         *
         *  Use SYSTEM account as auditor
         */
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            return userRepository.findFirstBySystemAccountTrue();
        }

        // Case: Logged-in user
        if (authentication.getPrincipal() instanceof CustomUserDetailsImpl userDetails) {

            return Optional.of(userDetails.getUser());
        }

        // Case: Unknown authentication type - fallback to SYSTEM user
        return userRepository.findFirstBySystemAccountTrue();
    }
}
