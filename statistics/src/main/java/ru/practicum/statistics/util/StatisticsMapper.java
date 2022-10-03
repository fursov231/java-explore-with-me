package ru.practicum.statistics.util;

import ru.practicum.statistics.dto.EndpointHit;
import ru.practicum.statistics.dto.ViewStats;
import ru.practicum.statistics.model.Stat;

public class StatisticsMapper {
    public static Stat hitToStat(EndpointHit endpointHit) {
        return Stat.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    //+set hits
    public static ViewStats toViewStats(Stat stat) {
        return ViewStats.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .build();
    }
}
