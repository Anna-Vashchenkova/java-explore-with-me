package ru.practicum.ewm_main_service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_main_service.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
