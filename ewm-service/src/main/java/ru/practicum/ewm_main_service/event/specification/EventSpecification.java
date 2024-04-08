package ru.practicum.ewm_main_service.event.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.event.model.Status;

import javax.persistence.criteria.Join;
import java.util.List;

public class EventSpecification {
    public static Specification<Event> userIdsIn(List<Long> users) {
        return (root, query, criteriaBuilder) -> {
            if (users == null) {
                return null;
            } else {
                Join<Object, Object> join = root.join("initiator");
                return join.get("id").in(users);
//                return criteriaBuilder.in(root.get("initiator.id").in(users));
            }
        };
    }

    public static Specification<Event> statusIn(List<Status> statusEnum) {
        return (root, query, criteriaBuilder) -> {
            if (statusEnum == null) {
                return null;
            } else {
                return criteriaBuilder.in(root.get("state").in(statusEnum));
            }
        };
    }


}
