package ru.practicum.ewm_main_service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.comment.dto.CommentDto;
import ru.practicum.ewm_main_service.comment.dto.NewCommentDto;
import ru.practicum.ewm_main_service.comment.dto.UpdateCommentUserRequest;
import ru.practicum.ewm_main_service.comment.service.CommentService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addNewComment(@PathVariable(name = "userId") long userId,
                                    @PathVariable(name = "eventId") long eventId,
                                    @Valid @RequestBody NewCommentDto dto) {
        log.info("Получен запрос на добавление нового комментария к событию с ID {} пользователем с ID {}", eventId, userId);
        CommentDto commentDto = commentService.addNewComment(userId, eventId, dto);
        log.info("Создан комментарий с ID={}", commentDto.getId());
        return commentDto;
    }

    @PatchMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable(name = "userId") long userId,
                                    @PathVariable(name = "eventId") long eventId,
                                    @PathVariable(name = "commentId") long commentId,
                                    @Valid @RequestBody UpdateCommentUserRequest dto) {
        log.info("Получен запрос на обновление комментария с ID {} к событию с ID {} пользователем с ID {}", commentId, eventId, userId);
        CommentDto commentDto = commentService.updateComment(userId, eventId, commentId, dto);
        log.info("Обновлен комментарий с ID={}", commentDto.getId());
        return commentDto;
    }

    @DeleteMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    public void deleteComment(@PathVariable(name = "userId") long userId,
                              @PathVariable(name = "eventId") long eventId,
                              @PathVariable(name = "commentId") long commentId) {
        log.info("Получен запрос на удаление комментария с ID {} к событию с ID {} пользователем с ID {}", commentId, eventId, userId);
        commentService.deleteComment(userId, eventId, commentId);
        log.info("Удален комментарий с ID={}", commentId);
    }
}
