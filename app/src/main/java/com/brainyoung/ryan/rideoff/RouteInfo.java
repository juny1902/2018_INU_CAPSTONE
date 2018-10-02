package com.brainyoung.ryan.rideoff;

public class RouteInfo {
    public String routeId; // 특정 버스에 고유적으로 부여되는 노선아이디를 받아오기 위한 변수
    public String busNumber;  // 버스번호
    public String busTypeName;  // 버스의 타입을 받아오는 변수(광역, 일반 등등)

    public RouteInfo(String routeId, String busNumber, String busTypeName) {
        this.routeId = routeId;
        this.busNumber = busNumber;
        this.busTypeName = busTypeName;
    }
}
