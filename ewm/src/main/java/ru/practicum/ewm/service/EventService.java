package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Comment;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.entity.dto.*;
import ru.practicum.ewm.entity.enums.SortType;
import ru.practicum.ewm.entity.enums.State;
import ru.practicum.ewm.entity.enums.StateAdminAction;
import ru.practicum.ewm.entity.enums.StateUserAction;
import ru.practicum.ewm.entity.mapper.CommentMapper;
import ru.practicum.ewm.entity.mapper.EventMapper;
import ru.practicum.ewm.error.exeptions.ConflictException;
import ru.practicum.ewm.error.exeptions.NotFoundException;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.repository.spec.EventSpecification;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final StatisticService statisticService;
    private final CommentRepository commentRepository;

    @Autowired
    public EventService(
            EventRepository eventRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            StatisticService statisticService,
            CommentRepository commentRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.statisticService = statisticService;
        this.commentRepository = commentRepository;
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
        return EventMapper.toDto(event, 0L);
    }

    @Transactional
    public EventFullDto patchEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
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
            if (event.getState() != State.PENDING && updateEventAdminRequest.getStateAction() == StateAdminAction.PUBLISH_EVENT) {
                throw new ConflictException("This event already published or canceled");
            }
            if (event.getState() == State.PUBLISHED && updateEventAdminRequest.getStateAction() == StateAdminAction.REJECT_EVENT) {
                throw new ConflictException("This event already published, you cant reject it");
            }
            event.setState(
                    updateEventAdminRequest.getStateAction() == StateAdminAction.PUBLISH_EVENT
                            ? State.PUBLISHED : State.CANCELED
            );
        }
        event = eventRepository.save(event);
        Long views = statisticService.getEventViews(eventId);
        return EventMapper.toDto(event, views);
    }


    public List<EventFullDto> getEventsAdmin(
            List<Long> users,
            List<State> states,
            List<Long> categoryIds,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    ) {
        Pageable pageable = PageRequest.of(from, size);
        Specification<Event> spec = Specification
                .where(EventSpecification.userIn(users))
                .and(EventSpecification.categoryIn(categoryIds))
                .and(EventSpecification.dateAfter(rangeStart))
                .and(EventSpecification.dateBefore(rangeEnd));
        if (states != null) {
            spec.and(EventSpecification.stateIn(states));
        }
        List<Event> events = eventRepository.findAll(spec, pageable).toList();
        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> viewsMap = statisticService.getEventsViews(eventIds);
        return EventMapper.toDtos(events, viewsMap);
    }

    public List<EventShortDto> getEventsUser(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Set<Event> events = eventRepository.findByInitiator_Id(userId, pageable).toSet();
        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> viewsMap = statisticService.getEventsViews(eventIds);
        return EventMapper.toShortDtos(events, viewsMap);
    }

    public List<EventShortDto> getEventsPublic(
            String text,
            List<Long> categoryIds,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            SortType sort,
            Integer from,
            Integer size
    ) {
        Pageable pageable = sort == SortType.EVENT_DATE ?
                PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, sort.getColumnName())) :
                PageRequest.of(from, size);

        Specification<Event> spec = Specification
                .where(EventSpecification.annotationTextLike(text).or(EventSpecification.descriptionTextLike(text)))
                .and(EventSpecification.categoryIn(categoryIds))
                .and(EventSpecification.equalPaid(paid))
                .and(EventSpecification.dateAfter(rangeStart))
                .and(EventSpecification.dateBefore(rangeEnd));
        Set<Event> events = eventRepository.findAll(spec, pageable).toSet();
        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getParticipantLimit() > e.getParticipations().size())
                    .collect(Collectors.toSet());
        }
        List<EventShortDto> eventShortDtos;
        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> viewsMap = statisticService.getEventsViews(eventIds);
        if (sort == SortType.VIEWS) {
            eventShortDtos = EventMapper.toShortDtosSorted(events, viewsMap);
        } else {
            eventShortDtos = EventMapper.toShortDtos(events, viewsMap);
        }
        return eventShortDtos;
    }

    public EventFullDto patchEventUser(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found")
        );
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("Event already published");
        }
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category with id=" + updateEventUserRequest.getCategory() + " was not found")
            );
            event.setCategory(category);
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLongitude(updateEventUserRequest.getLocation().getLon());
            event.setLatitude(updateEventUserRequest.getLocation().getLat());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            event.setState(
                    updateEventUserRequest.getStateAction() == StateUserAction.SEND_TO_REVIEW
                            ? State.PENDING : State.CANCELED
            );
        }
        event = eventRepository.save(event);
        Long views = statisticService.getEventViews(eventId);
        return EventMapper.toDto(event, views);
    }

    public EventFullDto getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found")
        );
        Long views = statisticService.getEventViews(eventId);
        return EventMapper.toDto(event, views);
    }

    public CommentDto createComment(Long userId, Long eventId, CommentRequestDto commentRequestDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found")
        );
        User author = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found")
        );
        Comment comment = CommentMapper.toObject(commentRequestDto, event, author);
        comment = commentRepository.save(comment);
        return CommentMapper.toDto(comment);
    }

    public List<CommentDto> getCommentsByEventId(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        return CommentMapper.toDtos(commentRepository.findByEvent_Id(eventId));
    }

    public void deleteComment(Long userId, Long eventId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with id=" + commentId + " was not found")
        );
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ConflictException("Forbidden");
        }
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new ConflictException("Event id=" + eventId + "dont have comment id=" + commentId);
        }
        commentRepository.delete(comment);
    }

    public CommentDto updateComment(Long userId, Long eventId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with id=" + commentId + " was not found")
        );
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ConflictException("Forbidden");
        }
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new ConflictException("Event id=" + eventId + "dont have comment id=" + commentId);
        }
        comment.setText(commentRequestDto.getText());
        comment = commentRepository.save(comment);
        return CommentMapper.toDto(comment);
    }

    public void deleteCommentAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with id=" + commentId + " was not found")
        );
        commentRepository.delete(comment);
    }
}
