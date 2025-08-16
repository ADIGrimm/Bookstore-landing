package online.bookstore.repository.comment.specifications;

import jakarta.persistence.criteria.Predicate;
import online.bookstore.model.Comment;
import online.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GeneralTextSpecificationProvider implements SpecificationProvider<Comment> {
    @Override
    public String getKey() {
        return "text";
    }

    @Override
    public Specification<Comment> getSpecification(String param) {
        return (root, query, cb) -> {
            String pattern = "%" + param.toLowerCase() + "%";
            Predicate textMatch = cb.like(cb.lower(root.get("text")), pattern);
            Predicate advantagesMatch = cb.like(cb.lower(root.get("advantages")), pattern);
            Predicate disadvantagesMatch = cb.like(cb.lower(root.get("disadvantages")), pattern);

            return cb.or(textMatch, advantagesMatch, disadvantagesMatch);
        };
    }
}
