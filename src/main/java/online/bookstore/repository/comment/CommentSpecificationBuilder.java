package online.bookstore.repository.comment;

import lombok.RequiredArgsConstructor;
import online.bookstore.dto.comment.CommentSearchParameters;
import online.bookstore.model.Comment;
import online.bookstore.repository.SpecificationBuilder;
import online.bookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentSpecificationBuilder
        implements SpecificationBuilder<Comment, CommentSearchParameters> {
    private final SpecificationProviderManager<Comment> commentSpecificationProviderManager;

    @Override
    public Specification<Comment> build(CommentSearchParameters searchParameters) {
        Specification<Comment> spec = Specification.where(null);

        if (searchParameters.text() != null && !searchParameters.text().isBlank()) {
            spec = spec.and(
                    commentSpecificationProviderManager.getSpecificationProvider("text")
                            .getSpecification(searchParameters.text())
            );
        }
        return spec;
    }
}
