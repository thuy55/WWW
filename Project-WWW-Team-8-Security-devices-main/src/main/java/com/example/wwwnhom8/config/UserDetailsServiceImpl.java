package com.example.wwwnhom8.config;


import com.example.wwwnhom8.entity.Customer;
import com.example.wwwnhom8.repository.CustomerReposiroty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private CustomerReposiroty customerReposiroty;

    @Override
    public UserDetails loadUserByUsername(String phone)
            throws UsernameNotFoundException {
        Customer customer = customerReposiroty.findPhone(phone);
        return new MyUserDetails(customer);
    }
}

