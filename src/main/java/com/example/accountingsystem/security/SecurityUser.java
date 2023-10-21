package com.example.accountingsystem.security;

import com.example.accountingsystem.entity.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class SecurityUser implements UserDetails {

    private final Employee employee;

    public SecurityUser(Employee employee) {
        this.employee = employee;
    }

    private GrantedAuthority getAuthority(String role) {
        return new SimpleGrantedAuthority(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections
                .singletonList(getAuthority(employee.getRole().name()));
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
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
}
