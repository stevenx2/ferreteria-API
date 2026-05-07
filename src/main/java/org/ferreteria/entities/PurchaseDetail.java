package org.ferreteria.entities;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
@Setter
@Getter
@Table(name = "DETALLE_COMPRA")
@Entity
public class PurchaseDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_compra")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Product product;

    @Column(name = "cantidad")
    private int quantity;
}
