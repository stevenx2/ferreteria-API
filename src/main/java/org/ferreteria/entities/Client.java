package org.ferreteria.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
/**
 * clientes de la ferreteria
 * */
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "CLIENTES")
public class Client {


    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @Column(name = "id_cliente")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @NotBlank(message = "el nombre es obligatorio")
    @Size(min = 3,max = 40,message = "el nombre del cliente debe estar entre 3 y 40 caracteres")
    @Column(name = "nombre")
    private String name;

    @EqualsAndHashCode.Include
    @ToString.Include
    @NotBlank(message = "el telefono es obligatorio")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe contener exactamente 10 dígitos")
    @Column(name = "telefono",length = 10)
    private String phoneNumber;

    @EqualsAndHashCode.Include
    @ToString.Include
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres")
    @Column(name = "direccion",length = 100)
    private String address;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "client",orphanRemoval = true,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Sale> sales = new ArrayList<>();


    public boolean addSale(Sale sale){
        sale.setClient(this);
        return sales.add(sale);
    }


    public void removeSale(Sale sale){sales.remove(sale);}



}
