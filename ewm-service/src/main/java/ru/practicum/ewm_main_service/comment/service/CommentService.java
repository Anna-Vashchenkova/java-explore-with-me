package ru.practicum.ewm_main_service.comment.service;

import ru.practicum.ewm_main_service.comment.dto.CommentDto;
import ru.practicum.ewm_main_service.comment.dto.NewCommentDto;
import ru.practicum.ewm_main_service.comment.dto.UpdateCommentUserRequest;

public interface CommentService {

    CommentDto addNewComment(long userId, long eventId, NewCommentDto dto);

    CommentDto updateComment(long userId, long eventId, long commentId, UpdateCommentUserRequest dto);

    void deleteComment(long userId, long eventId, long commentId);
}
