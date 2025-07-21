package com.example.ecommerce.Repository;



import com.example.ecommerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
    List<Product> findByCategory(String category);
    Page<Product> findAll(Pageable pageable);
}
