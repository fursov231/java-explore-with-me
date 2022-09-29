package ru.practicum.ewm.request.util;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

public class RequestMapper {
    public static ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static Request toRequest(ParticipationRequestDto participationRequestDto, Event event, User requester) {
        return Request.builder()
                .id(participationRequestDto.getId())
                .event(event)
                .created(participationRequestDto.getCreated())
                .requester(requester)
                .status(participationRequestDto.getStatus())
                .build();
    }
}
