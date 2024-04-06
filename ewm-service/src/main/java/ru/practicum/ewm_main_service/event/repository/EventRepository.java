package ru.practicum.ewm_main_service.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.event.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event as e " +
            "where e.initiator.id = :userId order by e.id")
    Page<Event> findAllByUserId(long userId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(long userId, long eventId);

    @Query("select e from Event as e " +
            "where e.initiator.id in (:users) " +
            "and e.state in (:statusEnum) " +
            "and e.category.id in (:categories) " +
            "and e.eventDate between :start and :end")
    Page<Event> findByParams(@Param ("users") @Nullable List<Long> users,
                             @Param ("statusEnum") @Nullable List<Status> statusEnum,
                             @Param ("categories") @Nullable List<Long> categories,
                             @Param("start") @Nullable LocalDateTime start,
                             @Param("end") @Nullable LocalDateTime end,
                             Pageable pageable);
}
