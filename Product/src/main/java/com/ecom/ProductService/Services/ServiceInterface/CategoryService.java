package com.ecom.ProductService.Services.ServiceInterface;

import com.ecom.CommonEntity.dtos.CategoryDto;
import com.ecom.CommonEntity.model.ResponseModel;

public interface CategoryService {

    public ResponseModel addCategory(CategoryDto categoryDto);
    public ResponseModel getAllCategory();
    public ResponseModel getCategory(long id);
    public ResponseModel updateCategory(CategoryDto categoryDto);
    public ResponseModel blockCategory(long id);

}
