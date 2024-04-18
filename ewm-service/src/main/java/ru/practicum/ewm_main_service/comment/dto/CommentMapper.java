package ru.practicum.ewm_main_service.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_main_service.comment.Comment;
import ru.practicum.ewm_main_service.event.dto.EventMapper;
import ru.practicum.ewm_main_service.user.dto.UserMapper;

import java.time.format.DateTimeFormatter;

@UtilityClass
public final class CommentMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CommentDto toCommentDto(final Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .owner(UserMapper.toUserShortDto(comment.getOwner()))
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .createdOn(comment.getCreatedOn() != null ? comment.getCreatedOn().format(formatter) : null)
                .build();
    }
}
