package ru.practicum.ewm.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.entity.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "participations", uniqueConstraints = {@UniqueConstraint(columnNames = {"requester", "event"})})
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event")
    private Event event;

    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}