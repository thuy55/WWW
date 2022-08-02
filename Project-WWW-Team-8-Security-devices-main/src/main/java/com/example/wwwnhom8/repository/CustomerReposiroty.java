package com.example.wwwnhom8.repository;

import com.example.wwwnhom8.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerReposiroty extends JpaRepository<Customer, Integer> {
    @Query(value = "select * from customer where phone = :x", nativeQuery = true)
    Customer findPhone(@Param("x") String phone);
}
