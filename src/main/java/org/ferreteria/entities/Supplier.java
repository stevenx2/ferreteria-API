package org.ferreteria.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Table(name = "proveedores")
@Entity
public class Supplier {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "nombre",length = 30)
    private String name;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "telefono")
    private String phoneNumber;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "direccion",length = 100)
    private String address;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "supplier",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();


    public boolean addProduct(Product product){
        product.setSupplier(this);
        return products.add(product);
    }


    public void removeProduct(Product product){products.remove(product);}
}
