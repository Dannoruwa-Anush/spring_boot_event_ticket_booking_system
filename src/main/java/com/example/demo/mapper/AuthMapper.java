package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.dto.responseDTO.LoginResponseDTO;
import com.example.demo.entity.User;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AuthMapper {

    LoginResponseDTO toLoginResponse(String token, User user);

}