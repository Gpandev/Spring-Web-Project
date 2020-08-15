package bg.eshop.web.controllers.cart;

import bg.eshop.domain.models.service.OrderItemServiceModel;
import bg.eshop.domain.models.service.OrderServiceModel;
import bg.eshop.domain.models.view.ItemDetailsViewModel;
import bg.eshop.domain.models.view.OrderItemViewModel;
import bg.eshop.domain.models.view.ShoppingBagItem;
import bg.eshop.service.ItemService;
import bg.eshop.service.OrderService;
import bg.eshop.service.UserService;
import bg.eshop.web.PageTitle;
import bg.eshop.web.controllers.BaseController;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {

    private final ItemService itemService;
    private final UserService userService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public CartController(ItemService itemService, UserService userService, OrderService orderService, ModelMapper modelMapper) {
        this.itemService = itemService;
        this.userService = userService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }


    @PostMapping("/add-product")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addToCartConfirm(String id, int quantity, HttpSession session) {
        ItemDetailsViewModel product = this.modelMapper
                .map(this.itemService.getItemById(id), ItemDetailsViewModel.class);

        OrderItemViewModel orderItemViewModel = new OrderItemViewModel();
        orderItemViewModel.setItem(product);
        orderItemViewModel.setPrice(product.getPrice());

        ShoppingBagItem cartItem = new ShoppingBagItem();
        cartItem.setItem(orderItemViewModel);
        cartItem.setQuantity(quantity);

        var cart = this.retrieveCart(session);
        this.addItemToCart(cartItem, cart);

        return super.redirect("/home");
    }

    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Cart Details")
    public ModelAndView cartDetails(ModelAndView modelAndView, HttpSession session) {
        var cart = this.retrieveCart(session);
        modelAndView.addObject("totalPrice", this.calcTotal(cart));

        return super.view("cart/cart-details", modelAndView);
    }

    @DeleteMapping("/remove-product")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView removeFromCartConfirm(String id, HttpSession session) {
        this.removeItemFromCart(id, this.retrieveCart(session));

        return super.redirect("/cart/details");
    }

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView checkoutConfirm(HttpSession session, Principal principal) {
        var cart = this.retrieveCart(session);

        OrderServiceModel orderServiceModel = this.prepareOrder(cart, principal.getName());
        this.orderService.createOrder(orderServiceModel);
        return super.redirect("/items/all");
    }

    private List<ShoppingBagItem> retrieveCart(HttpSession session) {
        this.initCart(session);
        List<ShoppingBagItem> currList = (List<ShoppingBagItem>) session.getAttribute("shopping-cart");
        return currList;
    }

    private void initCart(HttpSession session) {
        if (session.getAttribute("shopping-cart") == null) {
            session.setAttribute("shopping-cart", new LinkedList<>());
        }
    }

    private void addItemToCart(ShoppingBagItem item, List<ShoppingBagItem> cart) {
        for (ShoppingBagItem shoppingCartItem : cart) {
            if (shoppingCartItem.getItem().getItem().getId().equals(item.getItem().getItem().getId())) {
                shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() + item.getQuantity());
                return;
            }
        }

        cart.add(item);
    }

    private void removeItemFromCart(String id, List<ShoppingBagItem> cart) {
        cart.removeIf(ci -> ci.getItem().getItem().getId().equals(id));
    }

    private BigDecimal calcTotal(List<ShoppingBagItem> cart) {
        BigDecimal result = new BigDecimal(0);
        for (ShoppingBagItem item : cart) {
            result = result.add(item.getItem().getPrice().multiply(new BigDecimal(item.getQuantity())));
        }

        return result;
    }

    private OrderServiceModel prepareOrder(List<ShoppingBagItem> cart, String customer) {
        OrderServiceModel orderServiceModel = new OrderServiceModel();
        orderServiceModel.setCustomer(this.userService.getUserByUsername(customer));
        List<OrderItemServiceModel> items = new ArrayList<>();
        for (ShoppingBagItem item : cart) {
            OrderItemServiceModel itemServiceModel = this.modelMapper.map(item.getItem(), OrderItemServiceModel.class);

            for (int i = 0; i < item.getQuantity(); i++) {
                items.add(itemServiceModel);
            }
        }

        orderServiceModel.setItems(items);
        orderServiceModel.setTotalPrice(this.calcTotal(cart));

        return orderServiceModel;
    }
}
