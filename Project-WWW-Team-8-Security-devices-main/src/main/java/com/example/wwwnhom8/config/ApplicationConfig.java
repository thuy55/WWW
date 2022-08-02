package com.example.wwwnhom8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApplicationConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public UserDetailsService userDetailsService() {
        return new com.example.wwwnhom8.config.UserDetailsServiceImpl();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/css/**","/fonts/**","/img/**","/js/**","/login","/register","/forgotPassword","/","/home","/search","/sort","/product","/error").permitAll()
                .antMatchers("/cart","/cart/**","/profile/**","/history").hasAnyAuthority( "ROLE_CUSTOMER","ROLE_ADMIN")
                .antMatchers("/**").hasAnyAuthority( "ROLE_ADMIN")
//                .antMatchers().hasAnyAuthority( "ROLE_CUSTOMER")
//                .antMatchers("/history").hasAnyAuthority( "ROLE_CUSTOMER")



                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/", true)
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
        ;


    }

}