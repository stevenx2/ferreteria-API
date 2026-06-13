package org.ferreteria.utils;

import org.ferreteria.dto.SaleDetailDto;
import org.ferreteria.entities.SaleDetail;

/**
 * Esta clase contendrá métodos para mapear diferentes entidades de la base de datos a sus respectivos objetos dto
 * nota: Solo tiene el método de SaleDetail porque recién se me ocurrió que los servicios devolviera la entidad y yo mediante el controlador los mapeara.
 */
public class Mapper {


    /**
     * Mapea la entidad de SaleDetail a su clase DTO
     * @param saleDetail el objeto que se quiere mapear
     * @return el respectivo DTO mapeado
     */
    public static SaleDetailDto toSaleDetailDto(SaleDetail saleDetail){
        return new SaleDetailDto(
                saleDetail.getId(),
                saleDetail.getSale().getId(),
                saleDetail.getProduct().getId(),
                saleDetail.getQuantity()
        );
    }
}
