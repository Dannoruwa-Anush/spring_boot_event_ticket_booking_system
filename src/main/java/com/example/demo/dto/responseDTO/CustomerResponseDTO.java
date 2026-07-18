package com.example.demo.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerResponseDTO {
    private long id;
    private String address;
    private String date_of_birth;
    private String phoneNo;

    private UserResponseDTO user;
}
