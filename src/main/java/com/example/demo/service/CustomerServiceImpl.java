package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.CustomerRequestDTO;
import com.example.demo.dto.responseDTO.CustomerResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.User;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final UserRepository userRepository;
    private final CustomerMapper mapper;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    public CustomerServiceImpl(CustomerRepository repository, UserRepository userRepository, CustomerMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public PageResponseDTO<CustomerResponseDTO> getAllCustomers(Pageable pageable) {
        Page<Customer> customers = repository.findAll(pageable);
        List<CustomerResponseDTO> content = mapper.toResponseDTOList(customers.getContent());

        return new PageResponseDTO<>(
                content,
                customers.getNumber(),
                customers.getSize(),
                customers.getTotalElements(),
                customers.getTotalPages(),
                customers.isFirst(),
                customers.isLast());
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

        // Check duplicate email (exclude current user)
        if (userRepository.existsByEmailAndIdNot(
                customerRequestDTO.getUser().getEmail(),
                customer.getUser().getId())) {
            throw new RuntimeException("Email already exists");
        }

        // Update customer fields
        mapper.updateCustomerFromDto(customerRequestDTO, customer);

        // Update user fields (password is NOT updated)
        User user = customer.getUser();
        user.setName(customerRequestDTO.getUser().getName());
        user.setEmail(customerRequestDTO.getUser().getEmail());

        userRepository.save(user);

        Customer updated = repository.save(customer);

        logger.info("Customer updated successfully. ID: {}", updated.getId());

        return mapper.toResponseDTO(updated);
    }
}
