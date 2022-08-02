package com.example.wwwnhom8.entity.DTO;

import com.example.wwwnhom8.entity.Customer;
import com.example.wwwnhom8.entity.OrderDetail;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrdersDTO {

    private int id;

    private CustomerDTO customer;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private List<OrderDetailDTO> orderDetails;

    private double total;

    //Thanh toán hay chưa
    private boolean payment;
}
