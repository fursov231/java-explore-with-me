package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getAllRequests(long userId);

    ParticipationRequestDto addRequest(long userId, long eventId);

    void cancelRequest(long userId, long requestId);
}
