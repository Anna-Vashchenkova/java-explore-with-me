package ru.practicum.ewm_main_service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.comment.dto.CommentDto;
import ru.practicum.ewm_main_service.comment.dto.NewCommentDto;
import ru.practicum.ewm_main_service.comment.dto.UpdateCommentUserRequest;
import ru.practicum.ewm_main_service.comment.service.CommentService;
import ru.practicum.ewm_main_service.exception.ValidationException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addNewComment(
            @RequestHeader("X-Explore-User-Id") long userId,
            @PathVariable(name = "eventId") long eventId,
            @Valid @RequestBody NewCommentDto dto) {
        log.info("Получен запрос на добавление нового комментария к событию с ID {} пользователем с ID {}", eventId, userId);
        CommentDto commentDto = commentService.addNewComment(userId, eventId, dto);
        log.info("Создан комментарий с ID={}", commentDto.getId());
        return commentDto;
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getComments(
                                        @PathVariable(name = "eventId") long eventId,
                                        @RequestParam(name = "from", defaultValue = "0") int from,
                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен запрос на получение информации о комментариях к событию с ID {}", eventId);
        if ((from < 0) || (size < 1)) {
            throw new ValidationException("Неверные параметры запроса");
        }
        return commentService.getComments(eventId, from / size, size);
    }

    @GetMapping("/events/{eventId}/comments/{commentId}")
    public CommentDto getCommentById(
                                 @PathVariable(name = "eventId") long eventId,
                                 @PathVariable(name = "commentId") long commentId) {
        log.info("Получен запрос на получение информации о комментарии с ID {} к событию с ID {}", commentId, eventId);
        CommentDto commentDto = commentService.getCommentById(eventId, commentId);
        log.info("Получен комментарий с ID={}", commentDto.getId());
        return commentDto;
    }

    @PatchMapping("/events/{eventId}/comments/{commentId}")
    public CommentDto updateComment(@RequestHeader("X-Explore-User-Id") long userId,
                                    @PathVariable(name = "eventId") long eventId,
                                    @PathVariable(name = "commentId") long commentId,
                                    @Valid @RequestBody UpdateCommentUserRequest dto) {
        log.info("Получен запрос на обновление комментария с ID {} к событию с ID {} пользователем с ID {}", commentId, eventId, userId);
        CommentDto commentDto = commentService.updateComment(userId, eventId, commentId, dto);
        log.info("Обновлен комментарий с ID={}", commentDto.getId());
        return commentDto;
    }

    @DeleteMapping("/events/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestHeader("X-Explore-User-Id") long userId,
                              @PathVariable(name = "eventId") long eventId,
                              @PathVariable(name = "commentId") long commentId) {
        log.info("Получен запрос на удаление комментария с ID {} к событию с ID {} пользователем с ID {}", commentId, eventId, userId);
        commentService.deleteComment(userId, eventId, commentId);
        log.info("Удален комментарий с ID={}", commentId);
    }
}
