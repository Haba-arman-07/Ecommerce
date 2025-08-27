package com.ecom.CommonEntity.entities;

import com.ecom.CommonEntity.Enum.Role;
import com.ecom.CommonEntity.Enum.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(unique = true, nullable = false)
    private String mobile;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Address> address;

//    @OneToMany(mappedBy = "user")
//    @JsonIgnore
//    private List<Product> product;

    private String gender;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void saveValue (){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updateValue() {
        this.updatedAt = LocalDateTime.now();
    }

}