package com.example.wwwnhom8.repository;

import com.example.wwwnhom8.entity.Customer;
import com.example.wwwnhom8.entity.Order;
import com.example.wwwnhom8.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select * from orders where customer_id = :x and payment = 1 order by id desc", nativeQuery = true)
    public List<Order> getListOrderPaidbyCustomerId(@Param("x") int customerId);

    @Query(value = "select * from orders where customer_id = :x and payment = 0", nativeQuery = true)
    public Order getCartByCustomerId(@Param("x") int customerId);
    @Query(value = "select * from orders o inner join customer c on o.customer_id=c.id where c.phone like %:x% or c.user_name like %:x% order by date desc", nativeQuery = true)
    List<Order> findByPhone(@Param("x") String phone);
    @Query(value = "select * from orders order by date desc", nativeQuery = true)
    List<Order> findAllDesc();

    @Query(value = "select * from orders where customer_id = :customerId",nativeQuery = true)
    List<Order> getAllByCustomer(int customerId);
}
