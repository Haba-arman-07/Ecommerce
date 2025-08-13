package com.ecom.ProductService.dao;

import com.ecom.ProductService.Repository.*;
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
    private CartItemsRepository cartItemsRepo;

    @Autowired
    private CartRepository cartRepo;


}
