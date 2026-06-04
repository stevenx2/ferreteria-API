package org.ferreteria.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;



@EqualsAndHashCode
@ToString
@Setter
@Getter
@Table(name = "productos")
@Entity
@NoArgsConstructor
public class Product {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;


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
