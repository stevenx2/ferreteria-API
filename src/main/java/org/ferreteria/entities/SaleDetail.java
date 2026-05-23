package org.ferreteria.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
@Setter
@Getter
@Entity
@Table(name = "detalle_venta")
public class SaleDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Product product;


    @Column(name = "cantidad")
    private int quantity;
}
