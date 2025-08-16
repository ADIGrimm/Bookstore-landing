package online.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.exception.OrderProcessingException;
import online.bookstore.model.Book;
import online.bookstore.repository.book.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookQuantityService {
    private final BookRepository bookRepository;

    public Book updateQuantity(Long bookId, int val) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + bookId));
        book.setQuantity(book.getQuantity() + val);
        return bookRepository.save(book);
    }

    public void checkEnoughQuantity(Long bookId, int required) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + bookId));
        if (book.getQuantity() < required) {
            throw new OrderProcessingException("Not enough book: " + bookId);
        }
    }
}
