package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.dto.responseDTO.LoginResponseDTO;
import com.example.demo.entity.User;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AuthMapper {

    @Mapping(target = "user", ignore = true)
    LoginResponseDTO toLoginResponse(String token, User user);

    @Mapping(target = "user", ignore = true)
    LoginResponseDTO toPasswordChangeRequiredResponse(String token, User user);
}  