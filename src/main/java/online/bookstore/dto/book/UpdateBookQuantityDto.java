package online.bookstore.dto.book;

import jakarta.validation.constraints.NotNull;

public record UpdateBookQuantityDto(
        @NotNull
        Integer quantity
) {
}
