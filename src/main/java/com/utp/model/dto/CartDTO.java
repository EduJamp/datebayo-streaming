package com.utp.model.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartDTO {
    private List<ShoppingCartItemDTO> items = new ArrayList<>();

    public double getTotal() {
        double total = 0;
        for (ShoppingCartItemDTO item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void addItemShoppingCart(ShoppingCartItemDTO item){
        if (item != null) {
            this.items.add(item);
        }
    }

    public List<ShoppingCartItemDTO> getItems() {
        return Collections.unmodifiableList( items );
    }

    public void deleteItemShoppingCart(int id){
        if( items != null ){
            this.items.removeIf( e -> e.getIdProduct() == id );
        }
    }

    public void setItems(List<ShoppingCartItemDTO> items) {
        if (items != null) {
            this.items = new ArrayList<>(items);
        } else {
            this.items = new ArrayList<>();
        }
    }
}
