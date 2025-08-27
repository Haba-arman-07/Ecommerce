package com.ecom.ProductService.Services.ServiceInterface;

import com.ecom.CommonEntity.dtos.ProductDto;
import com.ecom.CommonEntity.model.ResponseModel;

public interface ProductService {

    ResponseModel getProduct(long id);

    ResponseModel getAllProduct(int page, int size);

    ResponseModel addProduct(ProductDto productDto);

    ResponseModel updateProduct(ProductDto productDto);

    ResponseModel blockProduct(long id);

    ResponseModel searchProduct(String keyword);

//    ResponseModel showProductDetail();

}
