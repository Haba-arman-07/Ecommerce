package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.entities.Category;
import com.ecom.CommonEntity.Enum.Status;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private long categoryId;
    private String categoryName;
    private Status status;

    public static Category toEntity(CategoryDto categoryDto) {
        return Category.builder()
                .categoryName(categoryDto.getCategoryName())
                .status(Status.ACTIVE)
                .build();
    }

    public static CategoryDto toDto(Category category){
        return CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .status(category.getStatus())
                .build();
    }
}
