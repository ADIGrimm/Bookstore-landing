package online.bookstore.repository.order;

import java.util.Optional;
import online.bookstore.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository
        extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @EntityGraph(attributePaths = {"orderItems.book"})
    Page<Order> findAllByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"orderItems.book"})
    Optional<Order> findById(Long id);

    @EntityGraph(attributePaths = {"orderItems.book"})
    Page<Order> findAll(Pageable pageable);
}
