package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.CustomerRequestDTO;
import com.example.demo.dto.responseDTO.CustomerResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public interface CustomerService {
    // Create will be handled in Auth
    PageResponseDTO<CustomerResponseDTO> getAllCustomers(Pageable pageable);
    CustomerResponseDTO getCustomerById(Long id);
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO);
    void deleteCustomer(Long id);
}
