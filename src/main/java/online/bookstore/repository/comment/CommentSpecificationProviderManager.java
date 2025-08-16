package online.bookstore.repository.comment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bookstore.model.Comment;
import online.bookstore.repository.SpecificationProvider;
import online.bookstore.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentSpecificationProviderManager implements SpecificationProviderManager<Comment> {
    private final List<SpecificationProvider<Comment>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Comment> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Can't find correct specification provider for key "
                                + key));
    }
}
