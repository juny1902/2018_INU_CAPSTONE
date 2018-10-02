package com.brainyoung.ryan.rideoff;

public class StationInfo {
    public String StationName; // 첫 화면에서 사용자가 모바일 정류장번호를 입력하여, 해당 모바일 정류소번호에따른 정류소 이름을 받아오기위한 변수
    public String RegionName;  // 정류소가 있는 지역을 가져오는 변수
    public String StationId;   // 정류소번호를 가져오기 위한 변수 

    public StationInfo(String StationName, String RegionName, String StationId) {
        this.StationName = StationName;
        this.RegionName = RegionName;
        this.StationId = StationId;
    }
}
