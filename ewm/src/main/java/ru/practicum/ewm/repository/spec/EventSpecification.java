package ru.practicum.ewm.repository.spec;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventSpecification {

    public static Specification<Event> userIn(List<Long> userIds) {
        return (root, query, builder) ->
                userIds != null ?
                        builder.in(root.get("initiator").in(userIds)) :
                        builder.conjunction();
    }

    public static Specification<Event> stateIn(List<State> states) {
        return (root, query, builder) -> {
            List<String> statesString = states.stream().map(Enum::toString).collect(Collectors.toList());
            return builder.in(root.get("state").in(statesString));
        };
    }

    public static Specification<Event> categoryIn(List<Long> categoryIds) {
        return (root, query, builder) ->
                categoryIds != null ?
                        builder.in(root.get("category").in(categoryIds)) :
                        builder.conjunction();
    }

    public static Specification<Event> dateAfter(LocalDateTime rangeStart) {
        return (root, query, builder) ->
                rangeStart != null ?
                        builder.greaterThan(root.get("eventDate"), rangeStart) :
                        builder.conjunction();
    }

    public static Specification<Event> dateBefore(LocalDateTime rangeEnd) {
        return (root, query, builder) ->
                rangeEnd != null ?
                        builder.lessThan(root.get("eventDate"), rangeEnd) :
                        builder.conjunction();
    }
}