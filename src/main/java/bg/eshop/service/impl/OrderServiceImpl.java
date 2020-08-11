package bg.eshop.service.impl;

import bg.eshop.domain.entities.Order;
import bg.eshop.domain.models.service.OrderServiceModel;
import bg.eshop.error.OrderNotFoundException;
import bg.eshop.repository.ItemRepository;
import bg.eshop.repository.OrderRepository;
import bg.eshop.service.OrderService;
import bg.eshop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository, UserService userService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createOrder(OrderServiceModel orderServiceModel) {
        orderServiceModel.setFinishedOn(LocalDateTime.now());

        this.orderRepository.saveAndFlush(this.modelMapper.map(orderServiceModel, Order.class));
    }

    @Override
    public Collection<OrderServiceModel> getAllOrders() {
        List<Order> orders = this.orderRepository.findAll();
        List<OrderServiceModel> orderServiceModels = orders
                .stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toList());

        return orderServiceModels;
    }

    @Override
    public Collection<OrderServiceModel> getOrdersByCustomer(String username) {
        return this.orderRepository.findAllByCustomer_UsernameOrderByOrderedOn(username)
                .stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderServiceModel getOrderById(String id) {
        return this.orderRepository.findById(id)
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .orElseThrow(() -> new OrderNotFoundException("Not found Order."));
    }
}
