package org.ferreteria.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * entidad de ventas realizadas a un cliente
 * */
@ToString
@EqualsAndHashCode
@Setter
@Getter
@Entity
@Table(name = "ventas")
public class Sale {


    @Id
    @Column(name = "id_venta")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha")
    private LocalDate date;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Client client;

}
