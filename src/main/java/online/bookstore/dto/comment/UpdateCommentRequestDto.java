package online.bookstore.dto.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequestDto(
        @NotBlank
        String text,
        @Min(1)
        @Max(5)
        Integer rating,
        String advantages,
        String disadvantages
) {
}
