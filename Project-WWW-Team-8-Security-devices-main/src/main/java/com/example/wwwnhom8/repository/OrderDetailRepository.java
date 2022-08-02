package com.example.wwwnhom8.repository;

import com.example.wwwnhom8.entity.OrderDetail;
import com.example.wwwnhom8.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Query(value = "select * from order_detail where product_id=:productId",nativeQuery = true)
    List<OrderDetail> findAllByProduct(int productId);
}
