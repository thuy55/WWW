package com.example.wwwnhom8.entity.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerDTO {

    private int id;
    private String userName;
    private String phone;
    private String password;
    private String address;
    private String email;
    private boolean gender;
    private String role;
    private List<OrdersDTO> orders;
}
