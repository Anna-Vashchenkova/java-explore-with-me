package ru.practicum.ewm_main_service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_main_service.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    Request findByRequesterIdAndId(Long userId, Long requestId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByIdIn(List<Long> requestIds);
}
