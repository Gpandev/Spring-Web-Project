package bg.eshop.service;

import bg.eshop.domain.models.service.OrderServiceModel;

import java.util.Collection;

public interface OrderService {

    void createOrder(OrderServiceModel orderServiceModel);

    Collection<OrderServiceModel> getAllOrders();

    Collection<OrderServiceModel> getOrdersByCustomer(String username);

    OrderServiceModel getOrderById(String id);
}
