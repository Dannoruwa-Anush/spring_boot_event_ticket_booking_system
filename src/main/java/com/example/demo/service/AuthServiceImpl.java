package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.enums.RoleTypeEnum;
import com.example.demo.dto.requestDTO.CustomerRequestDTO;
import com.example.demo.dto.requestDTO.LoginRequestDTO;
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

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AuthMapper authMapper, CustomerRepository customerRepository, UserRepository userRepository, RoleRepository roleRepository, CustomerMapper customerMapper, PasswordEncoder passwordEncoder) {
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
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

         try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequestDTO.getEmail(),
                                    loginRequestDTO.getPassword()));

            CustomUserDetailsImpl userDetails = (CustomUserDetailsImpl) authentication.getPrincipal();

            User user = userDetails.getUser();

            String token = jwtUtils.generateToken(userDetails);

            logger.info("User logged in successfully. User ID: {}, Email: {}", user.getId(), user.getEmail());

            return authMapper.toLoginResponse(token, user);

        } catch (AuthenticationException ex) {

            logger.warn("Failed login attempt for email: {}", loginRequestDTO.getEmail());

            throw ex;
        }
    }

    @Override
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

}
