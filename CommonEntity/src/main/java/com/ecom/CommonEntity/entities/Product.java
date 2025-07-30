package com.ecom.CommonEntity.entities;

import com.ecom.CommonEntity.Enum.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String description;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private Users user;

    private Double price;
    private int qty;
    private String imageUrl;
    private int taxRate;

    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void saveValue (){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
