package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.User;

public class CustomUserDetailsImpl implements UserDetails {

    private final User user;

    public CustomUserDetailsImpl(User user) {
        this.user = user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Role
        if (user.getRole() != null) {
            authorities.add(
                    new SimpleGrantedAuthority(
                            "ROLE_" + user.getRole().getName().name()));
        }

        // Staff Position Permissions
        if (user.getStaff() != null && user.getStaff().getPosition() != null) {
            user.getStaff()
                    .getPosition()
                    .getPositionPermissions()
                    .forEach(positionPermission -> {

                        if (positionPermission.getPermission() != null) {
                            authorities.add(
                                    new SimpleGrantedAuthority(
                                            positionPermission
                                                    .getPermission()
                                                    .getName()
                                                    .name()));
                        }
                    });
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isMustChangePassword() {
        return user.isMustChangePassword();
    }

    public User getUser() {
        return user;
    }
}
