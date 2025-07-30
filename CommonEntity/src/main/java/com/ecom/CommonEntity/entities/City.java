package com.ecom.CommonEntity.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long cityId;

    public String cityName;

//    @OneToMany(mappedBy = "city")
//    @JsonIgnore
//    private List<Address> address;

    @ManyToOne
    @JoinColumn(name = "stateId")
    private State state;

}
