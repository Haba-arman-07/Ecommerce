package com.ecom.commonRepo.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.ProductFeedDto;
import com.ecom.CommonEntity.entities.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Getter
@Setter
@Repository
public class ProductDao {

    @Autowired
    private MasterDao masterDao;

    public Product saveProduct(Product product){
        return masterDao.getProductRepo().save(product);
    }

    public Optional<Product> findById(Long id){
        return masterDao.getProductRepo().findById(id);
    }

    public Optional<Product> findProductByIdAndStatus(Long id,Status status){
        return masterDao.getProductRepo().findByProductIdAndStatus(id, status);
    }

    public Page<ProductFeedDto> getAllProducts(Pageable pageable){
        return masterDao.getProductRepo().fetchAllProducts(pageable);
    }

}
