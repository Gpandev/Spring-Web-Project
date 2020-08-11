package bg.eshop.domain.models.view;

import java.math.BigDecimal;

public class OrderItemViewModel {

    private ItemDetailsViewModel item;
    private BigDecimal price;

    public OrderItemViewModel() {
    }

    public ItemDetailsViewModel getItem() {
        return item;
    }

    public void setItem(ItemDetailsViewModel item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
