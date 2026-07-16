package com.example.demo.dto.requestDTO;

import com.example.demo.config.enums.RoleTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleRequestDTO {
    private RoleTypeEnum name;
}
