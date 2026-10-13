package com.utp.util.mappers;

import com.utp.model.dto.ProductCatalogDTO;
import com.utp.model.dto.ShoppingCartItemDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CatalogMapper {
    public static ProductCatalogDTO toProductCatalogDTO(ResultSet rs) throws SQLException {
         return new ProductCatalogDTO(
                rs.getInt("id_producto"),
                rs.getString("nombre_producto"),
                rs.getString("descripcion"),
                rs.getString("nombre_categoria"),
                rs.getDouble("precio"),
                rs.getString("imagen_url"),
                rs.getInt("cantidad_disponible"),
                rs.getBoolean("requiere_datos_cliente")
        );

    }

    public static ShoppingCartItemDTO toShoppingCartItemDTO(ResultSet rs, int quantity) throws SQLException {
        ShoppingCartItemDTO item = new ShoppingCartItemDTO();
        item.setIdProduct(rs.getInt("id_producto"));
        item.setIdProductVariant(null);
        item.setName(rs.getString("nombre"));
        item.setUnitPrice(rs.getDouble("precio"));
        item.setQuantity(quantity);

        return item;
    }
}
