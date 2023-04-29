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
                        root.get("initiator").in(userIds) :
                        builder.conjunction();
    }

    public static Specification<Event> stateIn(List<State> states) {
        return (root, query, builder) -> {
            List<String> statesString = states.stream().map(Enum::toString).collect(Collectors.toList());
            return root.get("state").in(statesString);
        };
    }

    public static Specification<Event> categoryIn(List<Long> categoryIds) {
        return (root, query, builder) ->
                categoryIds != null ? root.get("category").in(categoryIds) : builder.conjunction();
    }

    public static Specification<Event> dateAfter(LocalDateTime rangeStart) {
        return (root, query, builder) ->
                rangeStart != null ? builder.greaterThan(root.get("eventDate"), rangeStart) : builder.conjunction();
    }

    public static Specification<Event> dateBefore(LocalDateTime rangeEnd) {
        return (root, query, builder) ->
                rangeEnd != null ? builder.lessThan(root.get("eventDate"), rangeEnd) : builder.conjunction();
    }

    public static Specification<Event> annotationTextLike(String text) {
        return (root, query, builder) ->
                text != null ?
                        builder.like(builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%") :
                        builder.conjunction();
    }

    public static Specification<Event> descriptionTextLike(String text) {
        return (root, query, builder) ->
                text != null ?
                        builder.like(builder.lower(root.get("description")), "%" + text.toLowerCase() + "%") :
                        builder.conjunction();
    }

    public static Specification<Event> equalPaid(Boolean paid) {
        return (root, query, builder) ->
                paid != null ? builder.equal(root.get("paid"), paid) : builder.conjunction();
    }
}