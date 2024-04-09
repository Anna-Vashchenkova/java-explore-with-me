package ru.practicum.ewm_main_service.event.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.event.model.Status;

import javax.persistence.criteria.Join;
import java.time.LocalDateTime;
import java.util.List;

public class EventSpecification {
    public static Specification<Event> userIdsIn(List<Long> users) {
        return (root, query, criteriaBuilder) -> {
            if (users == null) {
                return null;
            } else {
                Join<Object, Object> join = root.join("initiator");
                return join.get("id").in(users);
            }
        };
    }

    public static Specification<Event> statusIn(List<Status> statusEnum) {
        return (root, query, criteriaBuilder) -> {
            if (statusEnum == null) {
                return null;
            } else {
                return criteriaBuilder.isTrue(root.get("state").in(statusEnum));
            }
        };
    }

    public static Specification<Event> categoryIdsIn(List<Long> categories) {
        return (root, query, criteriaBuilder) -> {
            if (categories == null) {
                return null;
            } else {
                Join<Object, Object> join = root.join("category");
                return join.get("id").in(categories);
            }
        };
    }

    public static Specification<Event> startAfter(LocalDateTime start) {
        return (root, query, criteriaBuilder) -> {
            if (start == null) {
                return null;
            } else {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), start);
            }
        };
    }

    public static Specification<Event> endBefore(LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            if (end == null) {
                return null;
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end);
            }
        };
    }
}
