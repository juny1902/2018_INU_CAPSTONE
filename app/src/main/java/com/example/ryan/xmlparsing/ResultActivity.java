package com.example.ryan.xmlparsing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class ResultActivity extends AppCompatActivity {
    ListView mListView;
    Button btn_ride, btn_off;
    ResultStationAdapter mResultStationAdapter = new ResultStationAdapter();
    ResultRunningAdapter mResultRunningAdapter = new ResultRunningAdapter();


    class stationRunningBusList {
        String seq;
        String plateNumber;

        stationRunningBusList(String seq, String plateNumber) {
            this.seq = seq;
            this.plateNumber = plateNumber;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 이전 인텐트에서 routeId 및 busNumber 가져옴.
        Intent curIntent = getIntent();
        final String routeId = curIntent.getStringExtra("routeId");
        final String busNumber = curIntent.getStringExtra("busNumber");
        final String sel_stationId = curIntent.getStringExtra("sel_stationId");

        // 뷰와 객체 연결
        btn_ride = findViewById(R.id.btn_ride);
        btn_off = findViewById(R.id.btn_off);
        mListView = findViewById(R.id.ListView_Results);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 타요 버튼
        btn_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        // 쿼리문 지정 (Route ID 이용)
        String uri_stations = "http://openapi.gbis.go.kr/ws/rest/busrouteservice/station?" +
                "serviceKey=i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&" +
                "routeId=" + routeId;
        Document document = null;
        try {
            // 문서 초기화
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri_stations);
            XPath xpath = XPathFactory.newInstance().newXPath();

            // XML 문서에서 필요한 정보 받아옴
            NodeList stationIdList = (NodeList) xpath.evaluate("//busRouteStationList/stationId", document, XPathConstants.NODESET);
            NodeList stationNameList = (NodeList) xpath.evaluate("//busRouteStationList/stationName", document, XPathConstants.NODESET);
            NodeList stationSeqList = (NodeList) xpath.evaluate("//busRouteStationList/stationSeq", document, XPathConstants.NODESET);

            // 어댑터에 Parsing 한 정보 넣기
            for (int i = 0; i < stationNameList.getLength(); i++) {
                mResultStationAdapter.addItem(
                        stationIdList.item(i).getTextContent(),
                        stationNameList.item(i).getTextContent(),
                        stationSeqList.item(i).getTextContent()
                );

            }
            for (int i = 0; i < stationNameList.getLength(); i++) {
                if(stationIdList.item(i).getTextContent().equals(sel_stationId)){
                    mResultStationAdapter.setBoldStation(Integer.parseInt(stationSeqList.item(i).getTextContent()));

                }
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = String.format("버스번호:%s\n노선번호:%s\n고유번호:%s\n이름:%s\n순서:%s",
                        busNumber, routeId,
                        mResultStationAdapter.getItem(position).stationId,
                        mResultStationAdapter.getItem(position).stationName,
                        mResultStationAdapter.getItem(position).stationSeq);
                Toast.makeText(ResultActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

        String uri_runnings = "http://openapi.gbis.go.kr/ws/rest/buslocationservice?serviceKey=" +
                "i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&" +
                "routeId=" + routeId;
        try {
            Document document_runnings = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri_runnings);
            XPath xpath_runnings = XPathFactory.newInstance().newXPath();
            NodeList plateNoList = (NodeList) xpath_runnings.evaluate("//busLocationList/plateNo", document_runnings, XPathConstants.NODESET);
            NodeList routeIdList = (NodeList) xpath_runnings.evaluate("//busLocationList/routeId", document_runnings, XPathConstants.NODESET);
            NodeList stationIdList = (NodeList) xpath_runnings.evaluate("//busLocationList/stationId", document_runnings, XPathConstants.NODESET);
            NodeList stationSeqList = (NodeList) xpath_runnings.evaluate("//busLocationList/stationSeq", document_runnings, XPathConstants.NODESET);
            int i = 0;
            for (i = 0; i < plateNoList.getLength(); i++) {
                mResultRunningAdapter.addItem(
                        plateNoList.item(i).getTextContent(),
                        routeIdList.item(i).getTextContent(),
                        stationIdList.item(i).getTextContent(),
                        stationSeqList.item(i).getTextContent());
            }

            ArrayList<stationRunningBusList> stationRunningSeqList = new ArrayList<>();
            for (i = 0; i < mResultRunningAdapter.getCount(); i++) {
                stationRunningSeqList.add(new stationRunningBusList(
                        mResultRunningAdapter.getItem(i).mStationSeq,
                        mResultRunningAdapter.getItem(i).mPlateNo));
            }
            for (i = 0; i < stationRunningSeqList.size(); i++) {
                mResultStationAdapter.setBusExists(Integer.parseInt(stationRunningSeqList.get(i).seq),
                        stationRunningSeqList.get(i).plateNumber,
                        true);
            }
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
