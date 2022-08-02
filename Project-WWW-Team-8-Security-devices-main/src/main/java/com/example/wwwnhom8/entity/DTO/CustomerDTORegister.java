package com.example.wwwnhom8.entity.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerDTORegister {

    private int id;
    private String userName;
    private String phone;
    private String password;
    private String password2;
    private String address;
    private String role;
    private boolean checkedPolicy;
    private boolean gender;
    private String email;


    private List<OrdersDTO> orders;
}
