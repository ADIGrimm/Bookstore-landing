package online.bookstore.repository.order;

import java.util.List;
import online.bookstore.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
    Page<OrderItem> findAllByOrderUserIdAndOrderId(Long userId, Long orderId, Pageable pageable);

    OrderItem findByOrderUserIdAndOrderIdAndId(Long userId, Long orderId, Long orderItemId);

    List<OrderItem> findAllByBookId(Long bookId);
}
