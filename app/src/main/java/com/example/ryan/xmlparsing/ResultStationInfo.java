package com.example.ryan.xmlparsing;

public class ResultStationInfo {
    String stationId; // 정류장 고유번호
    String stationName; // 정류장 이름
    String stationSeq;  // 정류장의 순서
    String currentPlateNumber; // 정류장에 정차한 버스 번호판 (비교용).
    Boolean visibility = false; // 버스 정차 여부
    Boolean isSelected = false;

    ResultStationInfo(String stationId, String stationName, String stationSeq){
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationSeq = stationSeq;
    }
}
