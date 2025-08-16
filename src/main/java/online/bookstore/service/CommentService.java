package online.bookstore.service;

import java.util.List;
import online.bookstore.dto.comment.CommentDto;
import online.bookstore.dto.comment.CommentSearchParameters;
import online.bookstore.dto.comment.CreateCommentRequestDto;
import online.bookstore.dto.comment.UpdateCommentRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto save(Long userId, Long bookId, CreateCommentRequestDto request);

    Page<CommentDto> getAllCommentsOfBook(Long taskId, Pageable pageable);

    CommentDto update(Long userId, Long commentId, UpdateCommentRequestDto request);

    void deleteById(Long userId, Long commentId);

    Page<CommentDto> getAll(Pageable pageable);

    void adminDeleteById(Long id);

    List<CommentDto> search(CommentSearchParameters searchParameters);
}
