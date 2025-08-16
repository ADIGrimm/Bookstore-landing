package online.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.comment.CommentDto;
import online.bookstore.dto.comment.CommentSearchParameters;
import online.bookstore.dto.comment.CreateCommentRequestDto;
import online.bookstore.dto.comment.UpdateCommentRequestDto;
import online.bookstore.service.CommentService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_USER')")
public class CommentController implements UserContextHelper {
    private final CommentService commentService;

    @Operation(summary = "Create comment",
            description = "Create comment")
    @PostMapping("/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(Authentication authentication, @PathVariable Long bookId,
                                    @Valid @RequestBody CreateCommentRequestDto requestDto) {
        return commentService.save(getUserId(authentication), bookId, requestDto);
    }

    @Operation(summary = "Get all comments of book",
            description = "Return list of comments of book as page")
    @GetMapping("/{bookId}")
    public Page<CommentDto> getAllByBook(@PathVariable Long bookId,
                                         @ParameterObject @PageableDefault Pageable pageable) {
        return commentService.getAllCommentsOfBook(bookId, pageable);
    }

    @Operation(summary = "Update comment information",
            description = "Update comment information")
    @PutMapping("/{id}")
    public CommentDto updateComment(Authentication authentication, @PathVariable Long id,
                              @Valid @RequestBody UpdateCommentRequestDto request) {
        return commentService.update(getUserId(authentication), id, request);
    }

    @Operation(summary = "Delete comment by id",
            description = "Delete comment by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(Authentication authentication, @PathVariable Long id) {
        commentService.deleteById(getUserId(authentication), id);
    }

    @Operation(summary = "Get all comments",
            description = "Return list of all comments as page")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<CommentDto> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        return commentService.getAll(pageable);
    }

    @Operation(summary = "Delete comment by id",
            description = "Delete comment by id")
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminDeleteComment(@PathVariable Long id) {
        commentService.adminDeleteById(id);
    }

    @Operation(summary = "Search comment",
            description = "Return search result for specified data")
    @PostMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<CommentDto> search(@RequestBody CommentSearchParameters searchParameters) {
        return commentService.search(searchParameters);
    }
}
