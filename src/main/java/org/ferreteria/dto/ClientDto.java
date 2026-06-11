package org.ferreteria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.ferreteria.entities.Client;


/**
 * Clase de tranferencia de datos que especifica reglas que deben cumplir los atributos.
 * Este es el objeto que se pasa en el cuerpo de una petición PUT cuando se quiere actualizar y crear un cliente
 */
@Data
@AllArgsConstructor
public class ClientDto {


    //constructor vacio para jackson
    public ClientDto(){

    }

    private Long id;

    @NotBlank(message = "{entities.error.name.blank}")
    @Size(min = 3,max = 40, message = "{entities.error.name.size}")
    private String name;

    @NotBlank(message = "{entities.error.phone.blank}")
    @Pattern(regexp = "\\d{10}",message = "{entities.error.phone.pattern}")
    private String phoneNumber;

    @NotBlank(message = "{client.error.address.blank}")
    @Size(max = 100,message = "{client.error.address.size}")
    private String address;
}
