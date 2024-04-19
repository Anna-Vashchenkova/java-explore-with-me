package ru.practicum.ewm_main_service.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_main_service.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment as c " +
    "where c.event.id = :eventId order by c.id")
    Page<Comment> findAll(long eventId, Pageable pageable);
}
