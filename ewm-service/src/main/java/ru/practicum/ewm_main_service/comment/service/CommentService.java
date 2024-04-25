package ru.practicum.ewm_main_service.comment.service;

import ru.practicum.ewm_main_service.comment.dto.CommentDto;
import ru.practicum.ewm_main_service.comment.dto.NewCommentDto;
import ru.practicum.ewm_main_service.comment.dto.UpdateCommentUserRequest;

import java.util.List;

public interface CommentService {

    CommentDto addNewComment(long userId, long eventId, NewCommentDto dto);

    List<CommentDto> getComments(long eventId, int from, int size);

    CommentDto getCommentById(long eventId, long commentId);

    CommentDto updateComment(long userId, long eventId, long commentId, UpdateCommentUserRequest dto);

    void deleteComment(long userId, long eventId, long commentId);
}
