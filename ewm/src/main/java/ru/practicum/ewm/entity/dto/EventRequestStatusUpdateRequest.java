package ru.practicum.ewm.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.entity.enums.RequestStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateRequest implements Serializable {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
