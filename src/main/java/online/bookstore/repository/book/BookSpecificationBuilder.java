package online.bookstore.repository.book;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.book.BookSearchParameters;
import online.bookstore.model.Book;
import online.bookstore.repository.SpecificationBuilder;
import online.bookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book, BookSearchParameters> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (searchParameters.author() != null && !searchParameters.author().isBlank()) {
            spec = spec.and(
                    bookSpecificationProviderManager.getSpecificationProvider("author")
                            .getSpecification(searchParameters.author())
            );
        }

        if (searchParameters.titlePart() != null && !searchParameters.titlePart().isBlank()) {
            spec = spec.and(
                    bookSpecificationProviderManager.getSpecificationProvider("title")
                            .getSpecification(searchParameters.titlePart())
            );
        }

        if (searchParameters.rating() != null) {
            spec = spec.and(
                    bookSpecificationProviderManager.getSpecificationProvider("rating")
                            .getSpecification(searchParameters.rating().toString())
            );
        }

        if (searchParameters.categoryIds() != null && !searchParameters.categoryIds().isEmpty()) {
            spec = spec.and(
                    bookSpecificationProviderManager.getSpecificationProvider("categoryIds")
                            .getSpecification(searchParameters.categoryIds().stream()
                                    .map(String::valueOf)
                                    .collect(Collectors.joining(",")))
            );
        }

        return spec;
    }
}
