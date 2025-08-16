package online.bookstore.repository.book.specifications;

import jakarta.persistence.criteria.Subquery;
import java.util.Arrays;
import java.util.List;
import online.bookstore.model.Book;
import online.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategoryIdsSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "categoryIds";
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        List<Long> categoryIds = Arrays.stream(param.split(","))
                .map(Long::valueOf)
                .toList();
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            var book = subquery.from(Book.class);
            var categories = book.join("categories");
            subquery.select(book.get("id"))
                    .where(cb.and(
                            cb.equal(book.get("id"), root.get("id")),
                            categories.get("id").in(categoryIds)
                    ))
                    .groupBy(book.get("id"))
                    .having(cb.equal(cb.countDistinct(categories.get("id")), categoryIds.size()));
            return cb.exists(subquery);
        };
    }
}
