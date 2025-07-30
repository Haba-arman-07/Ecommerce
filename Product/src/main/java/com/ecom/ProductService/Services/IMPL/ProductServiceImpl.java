package com.ecom.ProductService.Services.IMPL;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.ProductFeedDto;
import com.ecom.CommonEntity.dtos.ProductDto;
import com.ecom.CommonEntity.entities.Category;
import com.ecom.CommonEntity.entities.Product;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.Services.ServiceInterface.ProductService;
import com.ecom.ProductService.dao.CategoryDao;
import com.ecom.ProductService.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public ResponseModel addProduct(ProductDto productDto) {
//        List<Product> existProduct = productRepo.findAll();
        Optional<Category> exist = categoryDao.findCategoryByIdAndStatus(
                productDto.getCategory().getCategoryId(),Status.ACTIVE);

        if (exist.isPresent()) {
            Product product = ProductDto.toEntity(productDto);
            product.setCategory(exist.get());
            Product saveProduct = productDao.saveProduct(product);


            return new ResponseModel(
                    HttpStatus.OK,
                    ProductDto.toDto(saveProduct),
                    "Product Added Successfully"
            );

        } else {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Category Not Found"
            );
        }
    }

    @Override
    public ResponseModel getProduct(long id){
        Optional<Product> existProduct = productDao.findById(id);

        if (existProduct.isPresent()){
            Product product = existProduct.get();

            return new ResponseModel(
                    HttpStatus.OK,
                    ProductDto.toDto(product),
                    "SUCCESS"
            );
        } else {
            return new ResponseModel(
                    HttpStatus.BAD_REQUEST,
                    null,
                    "Product Not Exist"
            );
        }
    }

    @Override
    public ResponseModel getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductFeedDto> products = productDao.getAllProducts(pageable);
//        List<Product> products = masterDao.getProductRepo().findAllByStatus(Status.ACTIVE);

        return new ResponseModel(
                HttpStatus.OK,
                products.getContent(),
                "SUCCESS"
        );
    }

    @Override
    public ResponseModel updateProduct(ProductDto productDto){
        Optional<Product> existProduct = productDao.findProductByIdAndStatus(
                productDto.getProductId(), Status.ACTIVE);

        Optional<Category> existCategory = categoryDao.findCategoryByIdAndStatus(
                productDto.getCategory().getCategoryId(),Status.ACTIVE);

        if (existProduct.isPresent()) {
            if (existCategory.isPresent()) {
                Product product = existProduct.get();
                product.setProductName(productDto.getProductName());
                product.setDescription(productDto.getDescription());
                product.setPrice(productDto.getPrice());
                product.setQty(productDto.getQty());
                product.setImageUrl(productDto.getImageUrl());
                product.setTaxRate(productDto.getTaxRate());
                product.setCategory(existCategory.get());
                Product updateProduct = productDao.saveProduct(product);
                product.setUpdatedAt(LocalDateTime.now());

                return new ResponseModel(
                        HttpStatus.OK,
                        ProductDto.toDto(updateProduct),
                        "Product Update Successfully"
                );
            } else {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "Category Not Found"
                );
            }
        }
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Product Not Found"
            );
        }

    @Override
    public ResponseModel blockProduct(long id) {
        Optional<Product> existProduct = productDao.findProductByIdAndStatus(id,Status.ACTIVE);

        if (existProduct.isPresent()){
            Product product = existProduct.get();
            product.setStatus(Status.INACTIVE);
            Product save = productDao.saveProduct(product);

            return new ResponseModel(
                    HttpStatus.OK,
                    null,
                    "Product Deleted Successfully"
            );
        }
        return new ResponseModel(
                HttpStatus.BAD_REQUEST,
                null,
                "Product Not Found"
        );

    }


//    @Override
//    public ResponseModel showProductDetail() {
//        List<Product> product = masterDao.getProductRepo().findByProduct();
//
//         List<ProductDto> productDto = product.stream()
//                 .map(product1 -> ProductDto.toDto(product1))
//                 .toList();
//
//         return new ResponseModel(
//                 HttpStatus.OK,
//                 productDto,
//                 "SUCCESS"
//         );
//    }

}




