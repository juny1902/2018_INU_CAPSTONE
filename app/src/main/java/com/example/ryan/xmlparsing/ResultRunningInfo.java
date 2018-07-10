package com.example.ryan.xmlparsing;

public class ResultRunningInfo {
    String mPlateNo;
    String mRouteId;
    String mStationId;
    String mStationSeq;

    ResultRunningInfo(String plateNo, String routeId, String stationId, String stationSeq){
        this.mPlateNo = plateNo;
        this.mRouteId = routeId;
        this.mStationId = stationId;
        this.mStationSeq = stationSeq;
    }
}
