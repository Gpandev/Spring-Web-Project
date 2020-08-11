package bg.eshop.domain.models.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

public class OrderViewModel {

    private String id;
    private Collection<OrderItemViewModel> items;
    private UserViewModel customer;
    private BigDecimal totalPrice;
    private LocalDateTime orderedOn;


    public OrderViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<OrderItemViewModel> getItems() {
        return items;
    }

    public void setItems(Collection<OrderItemViewModel> items) {
        this.items = items;
    }

    public UserViewModel getCustomer() {
        return customer;
    }

    public void setCustomer(UserViewModel customer) {
        this.customer = customer;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDateTime orderedOn) {
        this.orderedOn = orderedOn;
    }
}
