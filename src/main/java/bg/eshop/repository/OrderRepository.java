package bg.eshop.repository;

import bg.eshop.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAllByCustomer_UsernameOrderByOrderedOn(String username);
}
