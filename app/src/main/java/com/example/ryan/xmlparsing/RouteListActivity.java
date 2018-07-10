package com.example.ryan.xmlparsing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class RouteListActivity extends AppCompatActivity {
    ListView mListView;
    Button btn_ride, btn_off;
    ResultStationAdapter mResultStationAdapter = new ResultStationAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 이전 인텐트에서 routeId 및 busNumber 가져옴.
        Intent curIntent = getIntent();
        String routeId = curIntent.getStringExtra("routeId");
        String busNumber = curIntent.getStringExtra("busNumber");

        // 뷰와 객체 연결
        btn_ride = findViewById(R.id.btn_ride);
        btn_off = findViewById(R.id.btn_off);
        mListView = findViewById(R.id.ListView_Results);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 쿼리문 지정 (Route ID 이용)
        String uri = "http://openapi.gbis.go.kr/ws/rest/busrouteservice/station?" +
                "serviceKey=i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&" +
                "routeId="+routeId;
        Document document = null;
        try {
            // 문서 초기화
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
            XPath xpath = XPathFactory.newInstance().newXPath();

            // XML 문서에서 필요한 정보 받아옴
            NodeList stationNameList = (NodeList) xpath.evaluate("//busRouteStationList/stationName", document, XPathConstants.NODESET);
            NodeList stationSeqList = (NodeList) xpath.evaluate("//busRouteStationList/stationSeq", document, XPathConstants.NODESET);

            // 어댑터에 Parsing 한 정보 넣기
            for (int i = 0; i < stationNameList.getLength(); i++) {
                mResultStationAdapter.addItem(
                        stationNameList.item(i).getTextContent(),
                        stationSeqList.item(i).getTextContent()
                );
            }

            // 리스트뷰에 어댑터 접속
            mListView.setAdapter(mResultStationAdapter);
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
