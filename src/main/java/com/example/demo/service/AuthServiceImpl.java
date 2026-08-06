package com.example.demo.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.enums.RoleTypeEnum;
import com.example.demo.dto.requestDTO.CustomerRequestDTO;
import com.example.demo.dto.requestDTO.ForgotPasswordRequestDTO;
import com.example.demo.dto.requestDTO.LoginRequestDTO;
import com.example.demo.dto.requestDTO.ResetPasswordRequestDTO;
import com.example.demo.dto.responseDTO.CustomerResponseDTO;
import com.example.demo.dto.responseDTO.LoginResponseDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetailsImpl;
import com.example.demo.security.jwt.JwtUtils;

import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

        private final AuthenticationManager authenticationManager;
        private final JwtUtils jwtUtils;
        private final AuthMapper authMapper;
        private final CustomerRepository customerRepository;
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final CustomerMapper customerMapper;
        private final PasswordEncoder passwordEncoder;

        // Logger for auditing purposes
        private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

        public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AuthMapper authMapper,
                        CustomerRepository customerRepository, UserRepository userRepository,
                        RoleRepository roleRepository,
                        CustomerMapper customerMapper, PasswordEncoder passwordEncoder) {
                this.authenticationManager = authenticationManager;
                this.jwtUtils = jwtUtils;
                this.authMapper = authMapper;
                this.customerRepository = customerRepository;
                this.userRepository = userRepository;
                this.roleRepository = roleRepository;
                this.customerMapper = customerMapper;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        @Transactional
        public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

                String email = loginRequestDTO.getEmail();

                try {
                        User user = userRepository.findByEmail(email)
                                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                        // Check account lock before authentication
                        if (user.isAccountLocked()) {
                                logger.warn("Login blocked. Account locked. Email: {}", email);
                                throw new LockedException("Account is temporarily locked. Try again later.");
                        }

                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        email,
                                                        loginRequestDTO.getPassword()));

                        CustomUserDetailsImpl userDetails = (CustomUserDetailsImpl) authentication.getPrincipal();

                        // Successful login
                        user.resetFailedLoginAttempts();
                        userRepository.save(user);

                        logger.info("User logged in successfully. User ID: {}, Email: {}", user.getId(),
                                        user.getEmail());

                        // Force password change for first login
                        if (user.isMustChangePassword()) {
                                String passwordChangeToken = jwtUtils.generatePasswordChangeToken(userDetails);
                                return authMapper.toPasswordChangeRequiredResponse(passwordChangeToken, user);
                        }

                        // Normal login
                        String token = jwtUtils.generateToken(userDetails);

                        return authMapper.toLoginResponse(token, user);

                } catch (BadCredentialsException ex) {

                        handleFailedLogin(email);

                        logger.warn("Invalid login attempt for email: {}", email);

                        throw ex;
                } catch (AuthenticationException ex) {

                        logger.warn("Authentication failed for email: {}", email);

                        throw ex;
                }
        }

        // Helper method
        private void handleFailedLogin(String email) {
                userRepository.findByEmail(email).ifPresent(user -> {

                        user.increaseFailedLoginAttempts();

                        // Lock account after 5 failed attempts for 15 minutes
                        if (user.getFailedLoginAttempts() >= 5) {
                                user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(15));
                                logger.warn("Account locked due to failed attempts. Email: {}", email);
                        }

                        userRepository.save(user);
                });
        }

        @Override
        @Transactional
        public CustomerResponseDTO registerCustomer(CustomerRequestDTO customerRequestDTO) {
                Role role = roleRepository.findByName(RoleTypeEnum.CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Role not found"));

                // Create a user
                User user = new User();
                user.setName(customerRequestDTO.getUser().getName());
                user.setEmail(customerRequestDTO.getUser().getEmail());
                user.setPassword(passwordEncoder.encode(customerRequestDTO.getUser().getPassword()));
                user.setRole(role);
                user = userRepository.save(user);

                // Create a customer
                Customer customer = customerMapper.toEntty(customerRequestDTO);
                customer.setUser(user);
                customer = customerRepository.save(customer);

                return customerMapper.toResponseDTO(customer);
        }

        @Override
        public void forgotPassword(ForgotPasswordRequestDTO dto) {
                User user = userRepository.findByEmail(dto.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                String token = jwtUtils.generatePasswordResetToken(
                                new CustomUserDetailsImpl(user));

                // Send email with reset link

                logger.info(
                                "Password reset requested for {}",
                                user.getEmail());
        }

        @Override
        @Transactional
        public void resetPassword(ResetPasswordRequestDTO dto) {

                String email = jwtUtils.extractUsername(dto.getToken());

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                CustomUserDetailsImpl userDetails = new CustomUserDetailsImpl(user);

                if (!jwtUtils.isPasswordResetToken(dto.getToken(), userDetails)) {
                        throw new RuntimeException("Invalid or expired reset token");
                }

                user.changePassword(
                                passwordEncoder.encode(dto.getNewPassword()));

                userRepository.save(user);

                logger.info("Password reset completed for {}", user.getEmail());
        }

        @Transactional
        public LoginResponseDTO changePassword(ResetPasswordRequestDTO dto) {

                String email = jwtUtils.extractUsername(dto.getToken());

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                CustomUserDetailsImpl userDetails = new CustomUserDetailsImpl(user);

                if (!jwtUtils.isPasswordChangeToken(dto.getToken(), userDetails)) {
                        throw new RuntimeException("Invalid or expired password change token");
                }

                user.changePassword(
                                passwordEncoder.encode(dto.getNewPassword()));

                userRepository.save(user);

                logger.info("Temporary password changed for {}", user.getEmail());

                String accessToken = jwtUtils.generateToken(
                                new CustomUserDetailsImpl(user));

                return authMapper.toLoginResponse(accessToken, user);
        }
}