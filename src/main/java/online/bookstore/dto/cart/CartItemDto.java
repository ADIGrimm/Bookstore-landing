package online.bookstore.dto.cart;

import online.bookstore.dto.book.BookDto;

public record CartItemDto(
        Long id,
        BookDto book,
        int quantity
) {
}
