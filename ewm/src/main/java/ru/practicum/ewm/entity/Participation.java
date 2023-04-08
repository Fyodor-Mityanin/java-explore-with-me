package ru.practicum.ewm.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.entity.enums.Status;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "participations")
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
    private Instant created;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}