package com.utp.controller.facade.interfaces;

import com.utp.model.dto.CartDTO;
import com.utp.model.dto.CredentialDeliveredDTO;
import com.utp.model.entity.PayEntity;
import com.utp.model.entity.SaleEntity;
import com.utp.model.entity.VoucherPayEntity;

import java.util.List;

public interface ICustomerPurchaseFacade {
    boolean makePurchase(int idClient, CartDTO cart, PayEntity pay, VoucherPayEntity voucher, double discountForCupon);
    List<SaleEntity> viewPurchaseHistory(int idClient);
    List<CredentialDeliveredDTO> accessMyPurchasedAccounts(int idSale);
}
