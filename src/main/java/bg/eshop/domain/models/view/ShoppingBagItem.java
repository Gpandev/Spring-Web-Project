package bg.eshop.domain.models.view;

import bg.eshop.domain.models.service.OrderItemServiceModel;

import java.io.Serializable;

public class ShoppingBagItem implements Serializable {

    private OrderItemViewModel item;
    private int quantity;

    public ShoppingBagItem() {
    }

    public OrderItemViewModel getItem() {
        return item;
    }

    public void setItem(OrderItemViewModel item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
