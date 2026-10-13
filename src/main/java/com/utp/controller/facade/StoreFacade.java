package com.utp.controller.facade;

import com.utp.controller.facade.interfaces.IStoreFacade;
import com.utp.model.dto.ProductCatalogDTO;
import com.utp.persistence.dao.CatalogDAO;

import java.sql.SQLException;
import java.util.List;

public class StoreFacade implements IStoreFacade {

    private final CatalogDAO catalogDAO = new CatalogDAO();

    @Override
    public List<ProductCatalogDTO> getCatalogAvailable() {
        try {
            return catalogDAO.showProductsInTheCatalog();
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductCatalogDTO showProductDetail(int idProduct) {
        try {
            return catalogDAO.getProductDetail(idProduct)
                    .orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
