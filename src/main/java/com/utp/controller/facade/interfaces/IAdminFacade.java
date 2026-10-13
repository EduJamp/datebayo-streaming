package com.utp.controller.facade.interfaces;

import com.utp.model.dto.*;
import com.utp.model.entity.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IAdminFacade {
    // Gestión de Catálogo e Inventario
    boolean addProduct(ProductEntity producto);
    boolean registerMainAccount(DigitalAccountEntity account, List<DigitalProfileEntity> profiles);

    // Gestión de Ventas y Pagos
    List<SaleDTO> getOrderTry();
    List<SaleDTO> getSalesForEmployee(int idEmployee);
    boolean validatePaymentAndShip(int idPay, int idSale, int idEmployeeValidator, String result);

    // Gestión de Cupones
    boolean createDiscountCampaing(String name, double percentaje, int idAdmin);
    boolean generateCouponForCustomer(int idCampania, int idClient, int idEmployeeGenerator);

    //Gestion de Productos
    List<ProductEntity> getAllProductsForAdmin() throws SQLException;
    List<CategoryEntity> getCategories() throws SQLException;
    List<ProductTypeEntity> getProductTypes() throws SQLException;
    boolean updateProduct(ProductEntity producto);
    boolean pauseProduct(int idProduct);

    List<Map<String, Object>> listarCuentasInventario();
    boolean editarCuentaInventario(DigitalAccountEntity account);
    boolean eliminarCuentaInventario(int idCuenta);
    List<CredentialDeliveredDTO> getCredentialsByClient(int idClient);

    List<CampaniaDTO> listarCampanias();
    boolean editarCampania(int idCampania, String nombre, double porcentaje, String estado);
    boolean eliminarCampania(int idCampania);
    List<CuponDTO> listarCupones();

    boolean generarCupon(int idCampania, String codigo, Integer limiteUsos, int idAdmin);
    boolean editarCupon(int idCupon, String codigo, Integer limiteUsos, String estado);
    boolean eliminarCupon(int idCupon);

    List<ClienteDTO> listarClientes();
    boolean editarCliente(int idUsuario, String nombres, String apellidos, String correo, String telefono, String estado);
    boolean bloquearCliente(int idUsuario);
}
