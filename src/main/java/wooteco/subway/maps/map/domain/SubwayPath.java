package wooteco.subway.maps.map.domain;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.maps.line.domain.Line;

public class SubwayPath {
    public static final int MIN_FARE = 1_250;

    private List<LineStationEdge> lineStationEdges;

    public SubwayPath(List<LineStationEdge> lineStationEdges) {
        this.lineStationEdges = lineStationEdges;
    }

    public List<LineStationEdge> getLineStationEdges() {
        return lineStationEdges;
    }

    public List<Long> extractStationId() {
        List<Long> stationIds = Lists
            .newArrayList(lineStationEdges.get(0).getLineStation().getPreStationId());
        stationIds.addAll(lineStationEdges.stream()
            .map(it -> it.getLineStation().getStationId())
            .collect(Collectors.toList()));

        return stationIds;
    }

    public int calculateDuration() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDuration()).sum();
    }

    public int calculateDistance() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDistance()).sum();
    }

    //TODO calculateFare 작성
    public int calculateTotalFare(List<Line> allLines) {
        List<Long> lineIds = getLineStationEdges()
            .stream()
            .map(LineStationEdge::getLineId)
            .collect(Collectors.toList());

        int basicFare = calculateBasicFare(calculateDistance());
        int maxExtraFare = findMaxExtraFare(allLines, lineIds);
        return basicFare + maxExtraFare;
    }

    private int calculateBasicFare(int distance) {
        if (distance <= 10) {
            return MIN_FARE;
        }
        return calculateOverFare(distance) + MIN_FARE;
    }

    private int calculateOverFare(int distance) {
        if (distance == 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int findMaxExtraFare(List<Line> allLines, List<Long> lineIds) {
        List<Integer> extraFares = new ArrayList<>();
        for (Long id : lineIds) {
            extraFares.add(allLines.stream()
                .filter(line -> line.getId().equals(id))
                .findFirst()
                .map(Line::getExtraFare)
                .orElse(0)
            );
        }
        return Collections.max(extraFares);
    }
}
