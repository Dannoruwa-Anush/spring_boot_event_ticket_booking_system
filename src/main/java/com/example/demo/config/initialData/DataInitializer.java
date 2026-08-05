package com.example.demo.config.initialData;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.config.enums.RoleTypeEnum;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDataBase() {
        return args -> {

            // Step 1: Create Roles
            Role systemRole = roleRepository.findByName(RoleTypeEnum.SYSTEM)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(RoleTypeEnum.SYSTEM);
                        return roleRepository.save(role);
                    });

            Role adminRole = roleRepository.findByName(RoleTypeEnum.ADMIN)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(RoleTypeEnum.ADMIN);
                        return roleRepository.save(role);
                    });

            roleRepository.findByName(RoleTypeEnum.STAFF)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(RoleTypeEnum.STAFF);
                        return roleRepository.save(role);
                    });   
            
                roleRepository.findByName(RoleTypeEnum.CUSTOMER)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(RoleTypeEnum.CUSTOMER);
                        return roleRepository.save(role);
                    });   

            // Step 2: Create system account
            userRepository.findFirstBySystemAccountTrue()
                    .orElseGet(() -> {

                        User system = new User();
                        system.setName("System");
                        system.setEmail("system@example.com");

                        // // impossible (random) login password
                        system.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                        system.setSystemAccount(true);
                        system.setRole(systemRole);

                        return userRepository.save(system);
                    });

            // Step 3: Create initial admin account
            userRepository.findByEmail("admin@example.com")
                    .orElseGet(() -> {
                        User adminUser = new User();
                        adminUser.setName("System Admin");
                        adminUser.setEmail("admin@example.com");
                        adminUser.setPassword(passwordEncoder.encode("ChangeMe123!"));
                        adminUser.setMustChangePassword(true);
                        adminUser.setRole(adminRole);

                        return userRepository.save(adminUser);
                    });
        };
    }
}