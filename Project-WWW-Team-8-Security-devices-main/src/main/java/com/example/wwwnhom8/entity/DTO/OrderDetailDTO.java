package com.example.wwwnhom8.entity.DTO;

import com.example.wwwnhom8.entity.Product;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailDTO {

    private int id;

    private OrdersDTO order;

    private ProductDTO product;

    private int quality;

}
