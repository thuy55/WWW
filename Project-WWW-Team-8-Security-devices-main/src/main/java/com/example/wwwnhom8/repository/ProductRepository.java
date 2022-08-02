package com.example.wwwnhom8.repository;

import com.example.wwwnhom8.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContains(@Param("name") String name);
    Product findByName(@Param("name") String name);

    @Query(value = "select * from product where name like :search", nativeQuery = true)
    List<Product> searchProduct(@Param("search") String search);

    @Query(value = "select * from product order by price asc", nativeQuery = true)
    List<Product> sortProductASC();

    @Query(value = "select * from product order by price desc", nativeQuery = true)
    List<Product> sortProductDESC();
}
