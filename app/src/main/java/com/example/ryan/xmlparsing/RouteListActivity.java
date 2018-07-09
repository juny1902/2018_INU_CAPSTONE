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
        Intent curIntent = getIntent();
        String routeId = curIntent.getStringExtra("routeId");
        String busNumber = curIntent.getStringExtra("busNumber");

        btn_ride = findViewById(R.id.btn_ride);
        btn_off = findViewById(R.id.btn_off);
        mListView = findViewById(R.id.ListView_Results);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        String uri = "http://openapi.gbis.go.kr/ws/rest/busrouteservice/station?" +
                "serviceKey=i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&" +
                "routeId="+routeId;
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList stationNameList = (NodeList) xpath.evaluate("//busRouteStationList/stationName", document, XPathConstants.NODESET);
            NodeList stationSeqList = (NodeList) xpath.evaluate("//busRouteStationList/stationSeq", document, XPathConstants.NODESET);
            for (int i = 0; i < stationNameList.getLength(); i++) {
                mResultStationAdapter.addItem(
                        stationNameList.item(i).getTextContent(),
                        stationSeqList.item(i).getTextContent()
                );
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
