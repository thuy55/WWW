package com.example.wwwnhom8.entity.DTO;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO {

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
