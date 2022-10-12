package ru.practicum.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statistics.dto.EndpointHit;
import ru.practicum.statistics.dto.ViewStats;
import ru.practicum.statistics.model.Stat;
import ru.practicum.statistics.repository.StatisticsRepository;
import ru.practicum.statistics.util.StatisticsMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public void save(EndpointHit endpointHit) {
        statisticsRepository.save(StatisticsMapper.hitToStat(endpointHit));
    }

    public List<ViewStats> findAllByParams(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> result = new ArrayList<>();
        List<Stat> stats = new ArrayList<>();
        if (!uris.isEmpty()) {
            if (!unique) {
                for (var uri : uris) {
                    stats.addAll(statisticsRepository.findAllByTimestampBetweenAndUri(start, end, uri));
                    result = toViewStats(stats);
                }
            } else {
                for (var uri : uris) {
                    stats.addAll(statisticsRepository.findAllByTimestampBetweenAndUriUnique(start, end, uri));
                    result = toViewStats(stats);
                }
            }
        }
        return result;
    }

    private List<ViewStats> toViewStats(List<Stat> stats) {
        List<ViewStats> viewStats = stats.stream().map(StatisticsMapper::toViewStats).collect(Collectors.toList());
        viewStats.forEach(e -> e.setHits((long) stats.size()));
        return new ArrayList<>(viewStats);
    }

    public Long findViews(String uri) {
       return statisticsRepository.findViews(uri);
    }
}
