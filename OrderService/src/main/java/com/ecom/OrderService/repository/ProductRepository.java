package com.ecom.OrderService.repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.ProductFeedDto;
import com.ecom.CommonEntity.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByStatus(Status status);

    Optional<Product> findByProductIdAndStatus(Long id,Status status);

//    @Query("SELECT new com.ecom.CommonEntity.dtos.ProductFeedDto(p.imageUrl, p.productId, p.productName," +
//            "c.categoryId, c.categoryName, p.price, p.status) FROM Product p join Category c " +
//            "where p.status = 'ACTIVE' AND c.status = 'ACTIVE' LIMIT 1,2")
//    List<Product> findByProduct();

    @Query("SELECT new com.ecom.CommonEntity.dtos.ProductFeedDto(" +
            "p.productId,p.productName,p.imageUrl,p.price," +
            "p.category.categoryId,p.category.categoryName,p.status) " +
            "FROM Product p WHERE p.status = 'ACTIVE' ")
    Page<ProductFeedDto> fetchAllProducts(Pageable pageable);

}
