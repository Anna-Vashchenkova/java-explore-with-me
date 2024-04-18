package ru.practicum.ewm_main_service.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.comment.Comment;
import ru.practicum.ewm_main_service.comment.dto.CommentDto;
import ru.practicum.ewm_main_service.comment.dto.CommentMapper;
import ru.practicum.ewm_main_service.comment.dto.NewCommentDto;
import ru.practicum.ewm_main_service.comment.dto.UpdateCommentUserRequest;
import ru.practicum.ewm_main_service.comment.repository.CommentRepository;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.event.model.Status;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.exception.ConflictException;
import ru.practicum.ewm_main_service.exception.DataNotFoundException;
import ru.practicum.ewm_main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserService userService;
    private final EventService eventService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public CommentDto addNewComment(long userId, long eventId, NewCommentDto dto) {
        if (userService.findById(userId).isEmpty()) {
            throw new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId));
        }
        Event event = eventService.findEventById(eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Событие с ID %s не найдено", eventId)));
        if (!event.getState().equals(Status.PUBLISHED)) {
            throw new ConflictException("Невозможно оставить комментарий к не опубликованному событию");
        }
        Comment comment = repository.save(new Comment(null,
                dto.getText(),
                userService.get(userId),
                event,
                LocalDateTime.now()));
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(long userId, long eventId, long commentId, UpdateCommentUserRequest dto) {
        userService.findById(userId).orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId)));
        eventService.findEventById(eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Событие с ID %s не найдено", eventId)));
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Комментарий с ID %s не найден", commentId)));
        comment.setText(dto.getText());
        repository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteComment(long userId, long eventId, long commentId) {
        userService.findById(userId).orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId)));
        eventService.findEventById(eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Событие с ID %s не найдено", eventId)));
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Комментарий с ID %s не найден", commentId)));
        repository.delete(comment);
    }
}
