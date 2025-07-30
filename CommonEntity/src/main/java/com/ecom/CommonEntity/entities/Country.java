package com.ecom.CommonEntity.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long countryId;

    public String countryName;

//    @OneToMany(mappedBy = "country")
//    @JsonIgnore
//    private List<Address> address;
//
//    @OneToMany(mappedBy = "country")
//    @JsonIgnore
//    private List<State> state;

}
