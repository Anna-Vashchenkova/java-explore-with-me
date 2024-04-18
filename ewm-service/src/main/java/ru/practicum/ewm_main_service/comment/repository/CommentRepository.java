package ru.practicum.ewm_main_service.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_main_service.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
