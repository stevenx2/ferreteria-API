package org.ferreteria.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
@Setter
@Getter
@Table(name = "PRODUCTOS")
@Entity
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;


    @NotBlank(message = "el nombre del producto no debe estar en blanco")
    @Size(min = 3,max = 30,message = "el nombre del producto debe estar entre 3 y 30 caracteres")
    @Column(name = "nombre")
    private String name;

    @Column(name = "precio")
    private double price;

    @Column(name = "stock")
    private int stock;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Supplier supplier;
}
