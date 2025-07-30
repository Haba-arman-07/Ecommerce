package com.ecom.CommonEntity.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long stateId;

    public String stateName;

    @ManyToOne
    @JoinColumn(name = "countryId")
    private Country country;

//    @OneToMany(mappedBy = "state")
//    @JsonIgnore
//    private List<Address> address;
//
//    @OneToMany(mappedBy = "state")
//    @JsonIgnore
//    private List<City> city;

}
