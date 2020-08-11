package bg.eshop.domain.models.service;

import java.math.BigDecimal;

public class OrderItemServiceModel extends BaseServiceModel {

    private ItemServiceModel item;
    private BigDecimal price;

    public OrderItemServiceModel() {
    }

    public ItemServiceModel getItem() {
        return item;
    }

    public void setItem(ItemServiceModel item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
