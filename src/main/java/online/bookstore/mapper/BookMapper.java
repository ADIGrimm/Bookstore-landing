package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.BookDtoWithoutCategoryIds;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "rating", source = "rating", qualifiedByName = "roundRating")
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "categories", ignore = true)
    void updateBookFromDto(CreateBookRequestDto bookRequestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Named("roundRating")
    static double roundRating(double rating) {
        return Math.round(rating * 10.0) / 10.0;
    }
}
