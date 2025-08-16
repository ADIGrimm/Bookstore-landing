package online.bookstore.repository.book.specifications;

import online.bookstore.model.Book;
import online.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class RatingSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "rating";
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        try {
            double min = Double.parseDouble(param);
            double max = min + 1;
            return (root, query, cb) -> cb.and(
                    cb.greaterThanOrEqualTo(root.get("rating"), min),
                    cb.lessThan(root.get("rating"), max)
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bad param form: " + param);
        }
    }
}
