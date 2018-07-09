package com.example.ryan.xmlparsing;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class BusRunningInfo {
    String busNumber;
    RouteInfo mRouteInfo;
    StationInfo mStationInfo;

    BusRunningInfo(String busNumber, RouteInfo routeInfo, StationInfo stationInfo) {
        this.busNumber = busNumber;
        this.mRouteInfo = routeInfo;
    }

    void getStationList() {
        String uri = "http://openapi.gbis.go.kr/ws/rest/busrouteservice/station?" +
                "serviceKey=i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&" +
                "routeId=" + mRouteInfo.routeId;
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList stationNameList = (NodeList) xpath.evaluate("//busRouteStationList/stationName", document, XPathConstants.NODESET);
            NodeList regionNameList = (NodeList) xpath.evaluate("//busRouteStationList/regionName", document, XPathConstants.NODESET);
            NodeList stationIdList = (NodeList) xpath.evaluate("//busRouteStationList/stationId", document, XPathConstants.NODESET);
            NodeList stationSeqList = (NodeList) xpath.evaluate("//busRouteStationList/stationSeq", document, XPathConstants.NODESET);
            for (int i = 0; i < stationNameList.getLength(); i++) {

            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
