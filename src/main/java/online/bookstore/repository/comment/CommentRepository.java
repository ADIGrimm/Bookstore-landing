package online.bookstore.repository.comment;

import java.util.List;
import java.util.Optional;
import online.bookstore.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository 
        extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    Optional<Comment> findByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = "user")
    Page<Comment> findAllByBookId(Long bookId, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    List<Comment> findAllByBookId(Long bookId);

    @EntityGraph(attributePaths = "user")
    List<Comment> findAll(Specification<Comment> commentSpecification);

    @EntityGraph(attributePaths = "user")
    Page<Comment> findAll(Pageable pageable);
}
