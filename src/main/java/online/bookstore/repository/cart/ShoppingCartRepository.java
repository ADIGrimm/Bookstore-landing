package online.bookstore.repository.cart;

import java.util.Optional;
import online.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShoppingCartRepository extends
        JpaRepository<ShoppingCart, Long>, JpaSpecificationExecutor<ShoppingCart> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book", "cartItems.book.categories"})
    Optional<ShoppingCart> findById(Long id);
}
