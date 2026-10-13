package com.utp.controller.facade;

import com.utp.controller.facade.interfaces.IAdminFacade;
import com.utp.model.dto.*;
import com.utp.model.entity.*;
import com.utp.persistence.dao.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdminFacade implements IAdminFacade {
    private final SaleDAO saleDAO =  new SaleDAO();
    private final PayDAO payDAO = new PayDAO();
    private final DispatchDAO dispatchDAO = new DispatchDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final CatalogDAO catalogDAO = new CatalogDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    public boolean addProduct(ProductEntity producto) {
        return catalogDAO.createProduct(producto);
    }

    @Override
    public boolean registerMainAccount(DigitalAccountEntity account, List<DigitalProfileEntity> profiles) {
        return inventoryDAO.registerDigitalAccount(account, profiles);
    }

    @Override
    public List<SaleDTO> getOrderTry() {
        return saleDAO.listGlobalSales("pago_en_revision");
    }

    @Override
    public List<SaleDTO> getSalesForEmployee(int idEmployee) {
        return saleDAO.listSaleByEmployee(idEmployee);
    }

    @Override
    public boolean validatePaymentAndShip(int idPay, int idSale, int idEmployeeValidator, String result) {
        boolean updatePay = payDAO.validatePayment(idPay, idEmployeeValidator, result);

        if (updatePay && result.equals("aprobado")) {
            saleDAO.changeSalesStatusAndAssingEmployee(idSale, "en_proceso", idEmployeeValidator);

            boolean despachado = dispatchDAO.assignAutomaticProfileToSale(idSale, idEmployeeValidator);

            if (despachado) {
                saleDAO.changeSalesStatusAndAssingEmployee(idSale, "entregada", idEmployeeValidator);
            }
        }
        return updatePay;
    }

    @Override
    public boolean createDiscountCampaing(String name, double percentaje, int idAdmin) {
        return catalogDAO.insertCampaing(name, percentaje, idAdmin);
    }

    @Override
    public boolean generateCouponForCustomer(int idCampania, int idClient, int idEmployeeGenerator) {
        return catalogDAO.assignCouponToCustomer(idCampania, idClient, idEmployeeGenerator);
    }

    @Override
    public List<ProductEntity> getAllProductsForAdmin() throws SQLException {
        return catalogDAO.getAllProductsForAdmin();
    }

    @Override
    public List<CategoryEntity> getCategories() throws SQLException {
        return catalogDAO.getCategories();
    }

    @Override
    public List<ProductTypeEntity> getProductTypes() throws SQLException {
        return catalogDAO.getProductTypes();

    }

    @Override
    public boolean updateProduct(ProductEntity producto) {
        return catalogDAO.updateProduct(producto);
    }

    @Override
    public boolean pauseProduct(int idProduct) {
        return catalogDAO.pauseProduct(idProduct);
    }

    @Override
    public List<Map<String, Object>> listarCuentasInventario() {
        return inventoryDAO.listInventoryAccounts();
    }

    @Override
    public boolean editarCuentaInventario(DigitalAccountEntity account) {
        return inventoryDAO.updateDigitalAccount(account);
    }

    @Override
    public boolean eliminarCuentaInventario(int idCuenta) {
        return inventoryDAO.deleteDigitalAccount(idCuenta);
    }

    @Override
    public List<CredentialDeliveredDTO> getCredentialsByClient(int idClient) {
        return inventoryDAO.getCredentialsByClient(idClient);
    }

    @Override
    public List<CampaniaDTO> listarCampanias() {
        return catalogDAO.listarCampanias();
    }

    @Override
    public boolean editarCampania(int idCampania, String nombre, double porcentaje, String estado) {
        return catalogDAO.editarCampania(idCampania, nombre, porcentaje, estado);
    }

    @Override
    public boolean eliminarCampania(int idCampania) {
        return catalogDAO.eliminarCampania(idCampania);
    }

    @Override
    public List<CuponDTO> listarCupones() {
        return catalogDAO.listarCupones();
    }

    @Override
    public boolean generarCupon(int idCampania, String codigo, Integer limiteUsos, int idAdmin) {
        return catalogDAO.generarCupon(idCampania, codigo, limiteUsos, idAdmin);
    }

    @Override
    public boolean editarCupon(int idCupon, String codigo, Integer limiteUsos, String estado) {
        return catalogDAO.editarCupon(idCupon, codigo, limiteUsos, estado);
    }

    @Override
    public boolean eliminarCupon(int idCupon) {
        return catalogDAO.eliminarCupon(idCupon);
    }

    @Override
    public List<ClienteDTO> listarClientes() {
        return userDAO.listarClientes();
    }

    @Override
    public boolean editarCliente(int idUsuario, String nombres, String apellidos, String correo, String telefono, String estado) {
        return userDAO.editarCliente(idUsuario, nombres, apellidos, correo, telefono, estado);
    }

    @Override
    public boolean bloquearCliente(int idUsuario) {
        return userDAO.bloquearCliente(idUsuario);
    }

}
