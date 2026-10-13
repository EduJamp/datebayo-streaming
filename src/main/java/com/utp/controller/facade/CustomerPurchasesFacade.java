package com.utp.controller.facade;

import com.utp.controller.facade.interfaces.ICustomerPurchaseFacade;
import com.utp.model.dto.CartDTO;
import com.utp.model.dto.CredentialDeliveredDTO;
import com.utp.model.entity.PayEntity;
import com.utp.model.entity.SaleEntity;
import com.utp.model.entity.VoucherPayEntity;
import com.utp.persistence.dao.DispatchDAO;
import com.utp.persistence.dao.PayDAO;
import com.utp.persistence.dao.SaleDAO;

import java.sql.SQLException;
import java.util.List;

public class CustomerPurchasesFacade implements ICustomerPurchaseFacade {

    private final SaleDAO saleDAO = new SaleDAO();
    private final PayDAO payDAO = new PayDAO();
    private final DispatchDAO dispatchDAO = new DispatchDAO();

    @Override
    public boolean makePurchase(int idClient, CartDTO cart, PayEntity pay, VoucherPayEntity voucher, double discountForCupon) {
        // Registra la venta y devuelve el ID
        int idVenta = saleDAO.registerSale(idClient, cart, discountForCupon);

        if (idVenta > 0) {
            // Vincula el pago (Yape/Plin/Efectivo) y la foto del voucher a la venta
            return payDAO.registerVoucher(idVenta, pay, voucher);
        }
        return false;
    }

    @Override
    public List<SaleEntity> viewPurchaseHistory(int idClient) {
        try {
            return saleDAO.viewPurchaseHistory(idClient);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CredentialDeliveredDTO> accessMyPurchasedAccounts(int idSale) {
        return dispatchDAO.getCredentialForSale(idSale);
    }
}
