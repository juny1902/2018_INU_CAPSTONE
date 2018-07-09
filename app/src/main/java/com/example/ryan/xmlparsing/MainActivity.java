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
    Button btn_search, btn_search_gbis; //첫 화면의 검색버튼, 정류소번호 찾기 버튼 ( * 정류소번호 찾기 사이트 : www.gbis.go.kr )
    EditText ed_mStation_id;  // 정류소번호를 입력하는 텍스트박스
    private ListView mListView_Stations; // 입력한 정류소번호에 대한 정류소를 보여주는 리스트뷰
    private ListView mListView_Bus;  // 정류소 중 내리고자하는 정류소를 클릭하고, 그 정류소를 운행하는 모든 버스들이 로드되는 리스트뷰

    final StationAdapter mStationAdapter = new StationAdapter(); // 위의 mListView_Stations 데이터를 받아오기 위한 어뎁터 선언
    final RouteAdapter mRouteAdapter = new RouteAdapter(); // 위의 mListView_Bus 데이터를 받아오기 위한 어뎁터 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ed_mStation_id = findViewById(R.id.edit_mStationId); // 정류소번호 검색 텍스트박스 가져오기
        btn_search_gbis = findViewById(R.id.btn_search_gbis); // 정류소번호 검색을 위한 버튼 아이디 가져오기
        btn_search = findViewById(R.id.btn_search); // 입력한 정류소 번호로 해당 정류소번호에따른 정류장을 가져오기위한 검색버튼 가져오기
        mListView_Stations = findViewById(R.id.ListView_Stations); // 정류장 리스트를 보여주는 리스트 뷰 가져오기

        btn_search_gbis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gbis_web_view = new Intent(getApplicationContext(), CustomWebView.class);
                startActivity(gbis_web_view);
            }
        });



        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mStationId = ed_mStation_id.getText().toString();// 입력한 정류소 번호를 가져와서 String 변수에 저장한다.

                try {  // 저장된 변수를 공공데이터 포털 서버에 요청메시지로 보내어, 입력한 정류소번호에 해당하는 정류소정보를 가져온다.
                    String uri = "http://openapi.gbis.go.kr/ws/rest/busstationservice?serviceKey=i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&keyword=" + mStationId;
                    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
                    XPath xpath = XPathFactory.newInstance().newXPath(); // xPath 를 사용하여 원하는 데이터를 분류하여 가져온다.
                    NodeList StationNameList = (NodeList) xpath.evaluate("//busStationList/stationName", document, XPathConstants.NODESET);
                    NodeList RegionNameList = (NodeList) xpath.evaluate("//busStationList/regionName", document, XPathConstants.NODESET);
                    NodeList StationIdList = (NodeList) xpath.evaluate("//busStationList/stationId", document, XPathConstants.NODESET);
                    for (int i = 0; i < StationNameList.getLength(); i++) {
                        mStationAdapter.addItem( // 가져와서 데이터를 저장
                                StationNameList.item(i).getTextContent(),
                                RegionNameList.item(i).getTextContent() + "시",
                                StationIdList.item(i).getTextContent()
                        );
                    }

                    mListView_Stations.setAdapter(mStationAdapter);

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

        mListView_Stations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListView_Bus = findViewById(R.id.ListView_Stations);
                String selectedStationId = mStationAdapter.getItem(position).StationId; // stationid(정류소번호)에 대한 정보를 담고있는 리스트를 클릭하여 해당정보를 공공데이터 포털 서버에 보내어
                                                                                            // 해당 정류장을 지나가는 버스리스트를 로드한다
                String uri = "http://openapi.gbis.go.kr/ws/rest/busstationservice/route?serviceKey=" +
                        "i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&" +
                        "stationId=" + selectedStationId;
                try {
                    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    final NodeList routeIdList = (NodeList) xpath.evaluate("//busRouteList/routeId", document, XPathConstants.NODESET);
                    final NodeList busNumberList = (NodeList) xpath.evaluate("//busRouteList/routeName", document, XPathConstants.NODESET);
                    NodeList busTypeList = (NodeList) xpath.evaluate("//busRouteList/routeTypeName", document, XPathConstants.NODESET);

                    for (int i = 0; i < routeIdList.getLength(); i++) {
                        mRouteAdapter.addItem(
                                routeIdList.item(i).getTextContent(),
                                busNumberList.item(i).getTextContent(),
                                busTypeList.item(i).getTextContent()
                        );
                    }
                    mListView_Bus.setAdapter(mRouteAdapter);
                    mListView_Bus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent resultIntent = new Intent(getApplicationContext(),RouteListActivity.class);
                            resultIntent.putExtra("routeId",mRouteAdapter.getItem(position).routeId);
                            resultIntent.putExtra("busNumber",mRouteAdapter.getItem(position).busNumber);
                            startActivity(resultIntent);
                        }
                    });


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
        });


    }
}
