package online.bookstore.dto.comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String firstName,
        String lastName,
        String text,
        LocalDateTime timestamp,
        int rating,
        String advantages,
        String disadvantages
) {
}
