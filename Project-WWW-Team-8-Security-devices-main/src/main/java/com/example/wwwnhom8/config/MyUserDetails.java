package com.example.wwwnhom8.config;

import com.example.wwwnhom8.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class MyUserDetails implements UserDetails {

    private Customer customer;

    public MyUserDetails(Customer user) {
        this.customer = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {


        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(customer.getRole()));
        return authorities;
    }

    @Override
    public String getPassword() {

        return "{noop}"+(customer!=null?customer.getPassword(): LocalDate.now().toString());
    }

    @Override
    public String getUsername() {
        return customer.getPhone();
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
