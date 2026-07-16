package com.example.demo.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // needed for JPA
@AllArgsConstructor
@Data // getters & setters
public class PositionRequestDTO {
    private String name;
}
