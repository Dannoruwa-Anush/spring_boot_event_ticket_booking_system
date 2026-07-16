package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.config.enums.RoleTypeEnum;
import com.example.demo.dto.requestDTO.CustomerRequestDTO;
import com.example.demo.dto.responseDTO.CustomerResponseDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerMapper mapper;
    private final PasswordEncoder passwordEncoder;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    public CustomerServiceImpl(CustomerRepository repository, UserRepository userRepository,
            RoleRepository roleRepository, CustomerMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
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
        Customer customer = mapper.toEntty(customerRequestDTO);
        customer.setUser(user);
        customer = repository.save(customer);

        return mapper.toResponseDTO(customer);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        List<Customer> customers = repository.findAll();
        return mapper.toResponseDTOList(customers);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return mapper.toResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        mapper.updateCustomerFromDto(customerRequestDTO, customer);

        Customer updated = repository.save(customer);

        logger.info("Customer updated successfully. ID: {}", updated.getId());

        return mapper.toResponseDTO(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Customer is not found with id: " + id);
        }

        repository.deleteById(id);

        logger.info("Customer deleted successfully. ID: {}", id);
    }
}
