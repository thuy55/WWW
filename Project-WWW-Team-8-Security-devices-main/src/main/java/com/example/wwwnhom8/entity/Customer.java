package com.example.wwwnhom8.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_name")
    private String userName;

    private String phone;
    private String password;
    private String address;
    private String email;
    private boolean gender;
    private String role;

    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY)
    private List<Order> orders;

}
