package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.dtos.StatisticDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpoint_hits")
@Getter
@Setter
@ToString
@NamedNativeQuery(
        name = "get_statistic",
        query = "select eh.app as app, eh.uri as uri, case when :unique then count(DISTINCT eh.ip) else count(*) end as hits " +
                "from endpoint_hits eh " +
                "where eh.hit_time between :start and :end " +
                "and :uris is null or eh.uri in :uris " +
                "group by eh.app, eh.uri " +
                "order by hits desc",
        resultSetMapping = "to_statistic_dto"
)
@SqlResultSetMapping(
        name = "to_statistic_dto",
        classes = @ConstructorResult(
                targetClass = StatisticDto.class,
                columns = {
                        @ColumnResult(name = "app", type = String.class),
                        @ColumnResult(name = "uri", type = String.class),
                        @ColumnResult(name = "hits", type = Long.class)
                }
        )
)
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String app;

    @Column
    private String uri;

    @Column
    private String ip;

    @Column
    private LocalDateTime hitTime;
}

