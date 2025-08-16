package online.bookstore.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.comment.CommentDto;
import online.bookstore.dto.comment.CommentSearchParameters;
import online.bookstore.dto.comment.CreateCommentRequestDto;
import online.bookstore.dto.comment.UpdateCommentRequestDto;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.mapper.CommentMapper;
import online.bookstore.model.Book;
import online.bookstore.model.Comment;
import online.bookstore.repository.book.BookRepository;
import online.bookstore.repository.comment.CommentRepository;
import online.bookstore.repository.comment.CommentSpecificationBuilder;
import online.bookstore.repository.user.UserRepository;
import online.bookstore.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CommentSpecificationBuilder commentSpecificationBuilder;

    @Override
    public CommentDto save(Long userId, Long bookId, CreateCommentRequestDto requestDto) {
        Comment comment = commentMapper.toModel(requestDto);
        comment.setUser(userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id " + userId)
        ));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + bookId));
        increaseBookRating(book, requestDto.rating());
        comment.setBook(book);
        comment.setTimestamp(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public Page<CommentDto> getAllCommentsOfBook(Long bookId, Pageable pageable) {
        if (bookRepository.findById(bookId).isPresent()) {
            return commentRepository.findAllByBookId(bookId, pageable).map(commentMapper::toDto);
        } else {
            throw new EntityNotFoundException("Can't find book by id " + bookId);
        }
    }

    @Override
    public CommentDto update(Long userId, Long commentId, UpdateCommentRequestDto request) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find comment by id " + commentId)
        );
        updateBookRating(comment.getBook(), comment.getRating(), request.rating());
        comment.setText(request.text());
        comment.setAdvantages(request.advantages());
        comment.setDisadvantages(request.disadvantages());
        comment.setRating(request.rating());
        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public void deleteById(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find comment by id " + commentId)
        );
        decreaseBookRating(comment.getBook(), comment.getRating());
        commentRepository.deleteById(commentId);
    }

    @Override
    public Page<CommentDto> getAll(Pageable pageable) {
        return commentRepository.findAll(pageable).map(commentMapper::toDto);
    }

    @Override
    public void adminDeleteById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find comment by id " + id));
        decreaseBookRating(comment.getBook(), comment.getRating());
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentDto> search(CommentSearchParameters searchParameters) {
        Specification<Comment> commentSpecification =
                commentSpecificationBuilder.build(searchParameters);
        return commentRepository.findAll(commentSpecification).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    private void increaseBookRating(Book book, int val) {
        double oldRating = book.getRating();
        int oldCommentCount = book.getCommentCount();
        int newCommentCount = oldCommentCount + 1;
        book.setCommentCount(newCommentCount);
        book.setRating(((oldRating * oldCommentCount) + val) / newCommentCount);
        bookRepository.save(book);
    }

    private void updateBookRating(Book book, int oldRating, int newRating) {
        double bookRating = book.getRating();
        int commentCount = book.getCommentCount();
        double newBookRating = (bookRating * commentCount - oldRating + newRating) / commentCount;
        book.setRating(newBookRating);
        bookRepository.save(book);
    }

    private void decreaseBookRating(Book book, int val) {
        double oldRating = book.getRating();
        int oldCommentCount = book.getCommentCount();
        int newCommentCount = oldCommentCount - 1;
        if (newCommentCount > 0) {
            double newRating = ((oldRating * oldCommentCount) - val) / newCommentCount;
            book.setRating(newRating);
            book.setCommentCount(newCommentCount);
        } else {
            book.setRating(0.0);
            book.setCommentCount(0);
        }
        bookRepository.save(book);
    }
}
