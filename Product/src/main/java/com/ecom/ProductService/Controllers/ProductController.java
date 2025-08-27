package com.ecom.ProductService.Controllers;

import com.ecom.CommonEntity.dtos.ProductDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.Services.ServiceInterface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseModel addProduct(@RequestBody ProductDto productDto){
        return productService.addProduct(productDto);
    }

    @GetMapping("/{id}")
    public ResponseModel getProduct(@PathVariable long id){
        return productService.getProduct(id);
    }

    @GetMapping("/")
    public ResponseModel getAllProduct(@RequestParam int page, @RequestParam int size){
        return productService.getAllProduct(page, size);
    }

    @PutMapping("/")
    public ResponseModel updateProduct(@RequestBody ProductDto productDto){
        return productService.updateProduct(productDto);
    }

    @PatchMapping("/{id}")
    public ResponseModel deleteProduct(@PathVariable long id){
        return productService.blockProduct(id);
    }

    @GetMapping("/search")
    public ResponseModel searchProducts(@RequestParam String keyword) {
        return productService.searchProduct(keyword);
    }

//    @GetMapping("/show")
//    public ResponseModel showProductDetail(){
//        return productService.showProductDetail();
//    }

}
