package com.ecom.ProductService.Controllers;

import com.ecom.CommonEntity.dtos.CategoryDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.Services.ServiceInterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/category/product")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/")
    public ResponseModel addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/")
    public ResponseModel getAllCategory(){
        return categoryService.getAllCategory();
    }

    @GetMapping("/{id}")
    public ResponseModel getCategory(@PathVariable long id){

        return categoryService.getCategory(id);
    }

    @PutMapping("/")
    public ResponseModel updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }

    @PatchMapping("/{id}")
    public ResponseModel deleteCategory(@PathVariable long id){
        return categoryService.blockCategory(id);
    }

//    @DeleteMapping("/a")
//    public ResponseModel deleteAllCategory(){
//        return categoryService.deleteAllCategory();
//    }

}
