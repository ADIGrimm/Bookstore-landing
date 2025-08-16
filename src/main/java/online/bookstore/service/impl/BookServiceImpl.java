package online.bookstore.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.BookSearchParameters;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.dto.book.UpdateBookQuantityDto;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.mapper.BookMapper;
import online.bookstore.model.Book;
import online.bookstore.model.Category;
import online.bookstore.repository.book.BookRepository;
import online.bookstore.repository.book.BookSpecificationBuilder;
import online.bookstore.repository.cart.CartItemRepository;
import online.bookstore.repository.category.CategoryRepository;
import online.bookstore.repository.comment.CommentRepository;
import online.bookstore.repository.order.OrderItemRepository;
import online.bookstore.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final CommentRepository commentRepository;
    private final BookQuantityService bookQuantityService;

    @Override
    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> search(BookSearchParameters params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);
        Set<Category> categories = new HashSet<>();
        for (Long category : bookRequestDto.getCategoryIds()) {
            categories.add(
                    categoryRepository.findById(category).orElseThrow(() ->
                            new EntityNotFoundException("Can't find category by id " + category)
                    ));
        }
        book.setCategories(categories);
        book.setRating(0);
        book.setCommentCount(0);
        book.setQuantity(0);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id));
        bookMapper.updateBookFromDto(bookRequestDto, book);
        Set<Category> updatedCategories = bookRequestDto.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId).orElseThrow(
                        () -> new EntityNotFoundException("Category not found: " + categoryId))
                )
                .collect(Collectors.toSet());
        book.setCategories(updatedCategories);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateQuantity(Long id, UpdateBookQuantityDto request) {
        return bookMapper.toDto(bookQuantityService.updateQuantity(id, request.quantity()));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.findAllByBookId(id)
                .forEach(el -> cartItemRepository.deleteById(el.getId()));
        orderItemRepository.findAllByBookId(id)
                .forEach(el -> orderItemRepository.deleteById(el.getId()));
        commentRepository.findAllByBookId(id)
                .forEach(el -> commentRepository.deleteById(el.getId()));
        bookRepository.deleteById(id);
    }
}
