package com.ecom.ProductService.dao;

import com.ecom.ProductService.Repository.CartRepository;
import com.ecom.ProductService.Repository.CategoryRepository;
import com.ecom.ProductService.Repository.ProductRepository;
import com.ecom.ProductService.Repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MasterDao {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CartRepository cartRepo;

}
