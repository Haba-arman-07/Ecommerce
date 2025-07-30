package com.ecom.ProductService.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Repository
public class CategoryDao {

    @Autowired
    private MasterDao masterDao;

    public Category saveCategory(Category category){
        return masterDao.getCategoryRepo().save(category);
    }

    public Optional<Category> findCategoryByIdAndStatus(long id, Status status){
        return masterDao.getCategoryRepo().findByCategoryIdAndStatus(id, status);
    }

    public List<Category> findCategoryByStatus(Status status){
        return masterDao.getCategoryRepo().findAllByStatus(status);
    }

    public Category findCategoryByName(String name){
        return masterDao.getCategoryRepo().findByCategoryName(name);
    }

}
