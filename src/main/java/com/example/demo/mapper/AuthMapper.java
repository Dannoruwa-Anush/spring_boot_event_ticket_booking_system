package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.dto.responseDTO.LoginResponseDTO;
import com.example.demo.entity.User;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AuthMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "mustChangePassword", source = "user.mustChangePassword")
    LoginResponseDTO toLoginResponse(String token, User user);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "mustChangePassword", constant = "true")
    LoginResponseDTO toPasswordChangeRequiredResponse(String token, User user);
}  