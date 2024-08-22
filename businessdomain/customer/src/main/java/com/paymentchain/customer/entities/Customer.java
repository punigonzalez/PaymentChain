package com.paymentchain.customer.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private String name;
    private String phone;
    private String surname;
    private String adress;
    private String iban;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "customer",cascade=CascadeType.ALL,orphanRemoval = true)
    private List<CustomerProduct> products;
    @Transient
    private List<?> transactions;

}
