package com.ecom.commonRepo.repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    //SELECT * FROM Category Where CategoryId = ?
    Category findByCategoryName(String name);

    List<Category> findAllByStatus(Status status);

    Optional<Category> findByCategoryIdAndStatus(long id, Status status);

//    Optional<Category> findByCategoryIdAndNameAndStatus(long categoryId, String name, Status status);

//    Optional<Category> findByStatus(Status status);
}
