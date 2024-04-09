package ru.practicum.ewm_main_service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_main_service.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);
}
