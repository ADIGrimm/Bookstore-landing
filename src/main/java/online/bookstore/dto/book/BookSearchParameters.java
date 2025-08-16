package online.bookstore.dto.book;

import java.util.Set;

public record BookSearchParameters(
        String titlePart,
        String author,
        Integer rating,
        Set<Long> categoryIds
) {

}
