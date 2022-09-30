package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.request.model.RequestState;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ParticipationRequestDto {
    Long id;
    Long event;
    LocalDateTime created;
    Long requester;
    RequestState status; //todo
}