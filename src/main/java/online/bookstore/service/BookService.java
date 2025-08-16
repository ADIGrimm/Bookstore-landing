package online.bookstore.service;

import java.util.List;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.BookSearchParameters;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.dto.book.UpdateBookQuantityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<BookDto> getAll(Pageable pageable);

    BookDto getById(Long id);

    List<BookDto> search(BookSearchParameters params);

    BookDto save(CreateBookRequestDto bookRequestDto);

    BookDto update(Long id, CreateBookRequestDto bookRequestDto);

    void deleteById(Long id);

    BookDto updateQuantity(Long id, UpdateBookQuantityDto request);
}
