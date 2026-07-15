package com.example.demo.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String headerAuth = request.getHeader("Authorization");

            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {

                String jwt = headerAuth.substring(7);

                String username = jwtUtils.extractUsername(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtils.isTokenValid(jwt, userDetails)) {

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                        authentication.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request));

                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);
                    }
                }
            }

        } catch (Exception ex) {

            logger.error("JWT Authentication failed: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}