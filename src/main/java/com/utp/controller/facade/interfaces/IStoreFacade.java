package com.utp.controller.facade.interfaces;

import com.utp.model.dto.ProductCatalogDTO;

import java.util.List;
import java.util.Optional;

public interface IStoreFacade {
    List<ProductCatalogDTO> getCatalogAvailable();
    ProductCatalogDTO showProductDetail(int idProduct);
}
