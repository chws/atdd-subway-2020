package wooteco.subway.maps.map.dto;

import wooteco.subway.maps.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    public static final int MIN_FARE = 1_250;

    private List<StationResponse> stations;
    private int duration;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int duration, int distance) {
        this.stations = stations;
        this.duration = duration;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    //TODO: 적절한 클래스로 옮겨줘야 함
    public int calculateFare() {
        if(distance <= 10) {
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
}
