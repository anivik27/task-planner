package org.vasya.task_planner.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.vasya.task_planner.dto.auth.UserDTO;
import org.vasya.task_planner.model.User;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements org.springframework.security.core.userdetails.UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String username;
    private final String password;
    private final Role role;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPasswordHash();
        this.role = user.getRole();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    public Long getId() {
        return this.id;
    }

    public UserDTO getUserDTO() {
        return new UserDTO(id, username, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}