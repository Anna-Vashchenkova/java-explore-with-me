package ru.practicum.ewm_main_service.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm_main_service.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query("select e from Event as e " +
            "where e.initiator.id = :userId order by e.id")
    Page<Event> findAllByUserId(long userId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(long userId, long eventId);
}
