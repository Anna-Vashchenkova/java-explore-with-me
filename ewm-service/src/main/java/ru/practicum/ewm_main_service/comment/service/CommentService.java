package ru.practicum.ewm_main_service.comment.service;

import ru.practicum.ewm_main_service.comment.dto.CommentDto;
import ru.practicum.ewm_main_service.comment.dto.NewCommentDto;

public interface CommentService {

    CommentDto addNewComment(long userId, long eventId, NewCommentDto dto);
}
