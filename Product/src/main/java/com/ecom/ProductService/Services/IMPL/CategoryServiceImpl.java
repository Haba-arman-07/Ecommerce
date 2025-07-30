package com.ecom.ProductService.Services.IMPL;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.CategoryDto;
import com.ecom.CommonEntity.entities.Category;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.Services.ServiceInterface.CategoryService;
import com.ecom.ProductService.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    // ADD CATEGORY
    @Override
    public ResponseModel addCategory(CategoryDto categoryDto){
        Category existCategory = categoryDao.findCategoryByName(categoryDto.getCategoryName());

        if (existCategory == null){
            Category category = CategoryDto.toEntity(categoryDto);
            Category saveCategory = categoryDao.saveCategory(category);

            return new ResponseModel(
                    HttpStatus.OK,
                    CategoryDto.toDto(saveCategory),
                    "Category Add Successfully"
            );
        } else {
            return new ResponseModel(
                    HttpStatus.BAD_REQUEST,
                    null,
                    "Category Already Exist"
            );
        }
    }

    // GET ALL CATEGORY
    @Override
    public ResponseModel getAllCategory(){
       List<Category> categories = categoryDao.findCategoryByStatus(Status.ACTIVE);

       List<CategoryDto> categoryDto = categories.stream()
               .map(CategoryDto::toDto)
               .toList();

       return new ResponseModel(
               HttpStatus.OK,
               categoryDto,
               "Success"
       );
    }

    // GET CATEGORY
    @Override
    public ResponseModel getCategory(long id){
        Optional<Category> existCategory = categoryDao.findCategoryByIdAndStatus(id,Status.ACTIVE);

        if (existCategory.isPresent()){
            Category category = existCategory.get();

            return new ResponseModel(
                    HttpStatus.OK,
                    CategoryDto.toDto(category),
                    "Category Get Success"
            );
        } else {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Category Not Found"
            );
        }
    }

    // UPDATE CATEGORY
    @Override
    public ResponseModel updateCategory(CategoryDto categoryDto){
        Optional<Category> existcategory = categoryDao.findCategoryByIdAndStatus(
                categoryDto.getCategoryId(),Status.ACTIVE);

        if (existcategory.isPresent()) {
            Category category = categoryDao.findCategoryByName(categoryDto.getCategoryName());
            if (category == null) {
                Category category1 = existcategory.get();
                category1.setCategoryName(categoryDto.getCategoryName());
                Category update = categoryDao.saveCategory(category1);

                return new ResponseModel(
                        HttpStatus.OK,
                        CategoryDto.toDto(update),
                        "Category Updated Successfully"
                );
            } else {
                return new ResponseModel(
                        HttpStatus.NOT_MODIFIED,
                        null,
                        "Category Name Already Exist"
                );
            }
        }
            return new ResponseModel(
                    HttpStatus.NOT_MODIFIED,
                    null,
                    "Category Not Exist"
            );
    }

    // DELETE CATEGORY
    @Override
    public ResponseModel blockCategory(long id){
        Optional<Category> existcategory = categoryDao.findCategoryByIdAndStatus(id,Status.ACTIVE);

        if (existcategory.isPresent()){
            Category category = existcategory.get();
            category.setStatus(Status.INACTIVE);
            categoryDao.saveCategory(category);

            return new ResponseModel(
                    HttpStatus.OK,
                    null,
                    "Category Deleted Successfully"
            );
        } else {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Category Not Found"
            );
        }
    }

}
