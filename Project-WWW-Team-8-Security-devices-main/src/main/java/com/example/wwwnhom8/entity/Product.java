package com.example.wwwnhom8.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    //Nhãn hiệu
    private String brand;
    //Loại camera
    private String type;
    //Chi tiết
    private String detail;
    //Giá
    private double price;
    //Số lượng
    private int quality;
    //Hình ảnh
    private String image;
}
