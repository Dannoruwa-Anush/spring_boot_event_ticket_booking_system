package com.example.demo.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerRequestDTO {
    private String address;
    private String date_of_birth;
    private String phoneNo;
    private UserRequestDTO user;
}
