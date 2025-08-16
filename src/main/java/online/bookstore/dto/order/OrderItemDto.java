package online.bookstore.dto.order;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private String bookTitle;
    private int quantity;
}
