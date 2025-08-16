package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.comment.CommentDto;
import online.bookstore.dto.comment.CreateCommentRequestDto;
import online.bookstore.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    CommentDto toDto(Comment comment);

    Comment toModel(CreateCommentRequestDto requestDto);
}
