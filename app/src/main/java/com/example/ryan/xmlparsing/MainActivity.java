package com.example.ryan.xmlparsing;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    Button btn_search, btn_search_gbis;
    EditText ed_mStation_id;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ed_mStation_id = findViewById(R.id.edit_mStationId);
        btn_search_gbis = findViewById(R.id.btn_search_gbis);
        btn_search = findViewById(R.id.btn_search);
        mListView = findViewById(R.id.ListView_Stations);

        btn_search_gbis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gbis_web_view = new Intent(getApplicationContext(), CustomWebView.class);
                startActivity(gbis_web_view);
            }
        });

        final MyAdapter mMyAdapter = new MyAdapter();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mStationId = ed_mStation_id.getText().toString();

                try {
                    String uri = "http://openapi.gbis.go.kr/ws/rest/busstationservice?serviceKey=i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&keyword=" + mStationId;
                    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    NodeList StationNameList = (NodeList) xpath.evaluate("//busStationList/stationName", document, XPathConstants.NODESET);
                    NodeList RegionNameList = (NodeList) xpath.evaluate("//busStationList/regionName", document, XPathConstants.NODESET);
                    NodeList StationIdList = (NodeList) xpath.evaluate("//busStationList/stationId", document, XPathConstants.NODESET);
                    for (int i = 0; i < StationNameList.getLength(); i++) {
                        mMyAdapter.addItem(
                                StationNameList.item(i).getTextContent(),
                                RegionNameList.item(i).getTextContent() + "시",
                                StationIdList.item(i).getTextContent()
                        );
                    }

                    mListView.setAdapter(mMyAdapter);

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStationId = mMyAdapter.getItem(position).StationId;
                String uri = "hhttp://openapi.gbis.go.kr/ws/rest/busstationservice/route?serviceKey=" +
                        "i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&" +
                        "stationId=" + selectedStationId;
                try {
                    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    NodeList routeIdList = (NodeList) xpath.evaluate("//busRouteList/routeId", document, XPathConstants.NODESET);
                    NodeList busNumberList = (NodeList) xpath.evaluate("//busRouteList/routeName", document, XPathConstants.NODESET);
                    NodeList busTypeList = (NodeList) xpath.evaluate("//busRouteList/routeTypeName", document, XPathConstants.NODESET);

                    for (int i = 0; i < routeIdList.getLength(); i++) {
                        mMyAdapter.addItem(
                                routeIdList.item(i).getTextContent(),
                                busNumberList.item(i).getTextContent() + "시",
                                busTypeList.item(i).getTextContent()
                        );
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
                XPath xpath = XPathFactory.newInstance().newXPath();


            }
        });


    }
}