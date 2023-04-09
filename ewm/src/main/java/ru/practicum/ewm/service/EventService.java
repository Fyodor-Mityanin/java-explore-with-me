package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.entity.dto.EventFullDto;
import ru.practicum.ewm.entity.dto.NewEventDto;
import ru.practicum.ewm.entity.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.entity.enums.State;
import ru.practicum.ewm.entity.enums.StateAction;
import ru.practicum.ewm.entity.mapper.EventMapper;
import ru.practicum.ewm.error.exeptions.NotFoundException;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.repository.spec.EventSpecification;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Category with id=" + newEventDto.getCategory() + " was not found")
        );
        User requester = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found")
        );
        Event event = EventMapper.toObject(category, requester, newEventDto, State.PENDING);
        event = eventRepository.save(event);
        return EventMapper.toDto(event);
    }

    @Transactional
    public EventFullDto patchEvent(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found")
        );
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category with id=" + updateEventAdminRequest.getCategory() + " was not found")
            );
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLongitude(updateEventAdminRequest.getLocation().getLon());
            event.setLatitude(updateEventAdminRequest.getLocation().getLat());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            event.setState(
                    updateEventAdminRequest.getStateAction() == StateAction.PUBLISH_EVENT
                            ? State.PUBLISHED : State.CANCELED
            );
        }
        event = eventRepository.save(event);
        return EventMapper.toDto(event);
    }


    public List<EventFullDto> getEvents(
            List<Long> userIds,
            List<State> states,
            List<Long> categoryIds,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    ) {
        Pageable pageable = PageRequest.of(from, size);
        Specification<Event> spec = Specification
                .where(EventSpecification.userIn(userIds))
                .and(EventSpecification.categoryIn(categoryIds))
                .and(EventSpecification.dateAfter(rangeStart))
                .and(EventSpecification.dateBefore(rangeEnd));
        if (states != null) {
            spec.and(EventSpecification.stateIn(states));
        }
        Page<Event> events1 = eventRepository.findAll(spec, pageable);
        return EventMapper.toDtos(events1.toList());
    }
}
