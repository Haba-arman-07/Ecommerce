package com.ecom.ProductService.Services.IMPL;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.ProductFeedDto;
import com.ecom.CommonEntity.dtos.ProductDto;
import com.ecom.CommonEntity.entities.Category;
import com.ecom.CommonEntity.entities.Product;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.Services.ServiceInterface.ProductService;
import com.ecom.commonRepo.dao.CategoryDao;
import com.ecom.commonRepo.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    @CacheEvict(value = {"getAllProduct","getProduct"}, allEntries = true)
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
    @Cacheable(value = "getProduct", key = "#id")
    public ResponseModel getProduct(long id){

        Optional<Product> existProduct = productDao.findById(id);
        System.out.println("Product Get In DB...");

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
    @Cacheable(value = "getAllProduct",key = "#page + '_' + #size")
    public ResponseModel getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductFeedDto> products = productDao.getAllProducts(pageable);
//        List<Product> products = masterDao.getProductRepo().findAllByStatus(Status.ACTIVE);
        System.out.println("All Product Get In DB..");

        return new ResponseModel(
                HttpStatus.OK,
                products.getContent(),
                "SUCCESS"
        );
    }

    @Override
    @CachePut(value = "getProduct",key = "#productDto.productId")
    @CacheEvict(value = "getAllProduct", allEntries = true)
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
    @CacheEvict(value = {"getProduct","getAllProduct"}, allEntries = true)
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


    @Override
    public ResponseModel searchProduct(String keyword) {
        List<Product> products = productDao.searchProducts(keyword, Status.ACTIVE);

        if (products.isEmpty()) {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "No products found for keyword: " + keyword
            );
        }

        List<ProductDto> result = products.stream()
                .map(ProductDto::toDto)
                .toList();

        return new ResponseModel(
                HttpStatus.OK,
                result,
                "SUCCESS"
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




