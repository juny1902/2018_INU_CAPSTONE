package com.example.ryan.xmlparsing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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

public class RouteActivity extends AppCompatActivity {
    private ListView mListView_Route;  // 정류소 중 내리고자하는 정류소를 클릭하고, 그 정류소를 운행하는 모든 버스들이 로드되는 리스트뷰
    final RouteAdapter mRouteAdapter = new RouteAdapter(); // 위의 mListView_Bus 데이터를 받아오기 위한 어뎁터 선언
    EditText ed_mStation_routes;
    Button btn_search_gbis_routes;
    Button btn_search_routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        final Intent mIntent = getIntent();
        final String StationId = mIntent.getStringExtra("StationId");
        final String mStationId = mIntent.getStringExtra("mStationId");
        mListView_Route = findViewById(R.id.ListView_Routes);
        btn_search_gbis_routes = findViewById(R.id.btn_search_gbis_routes);
        btn_search_routes = findViewById(R.id.btn_search_routes);
        ed_mStation_routes = findViewById(R.id.edit_mStationId_routes);

        ed_mStation_routes.setText(mStationId);

        ed_mStation_routes.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    btn_search_routes.callOnClick();
                }
                return false;
            }
        });
        btn_search_gbis_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gbis_web_view = new Intent(getApplicationContext(), CustomWebView.class);
                startActivity(gbis_web_view);
            }
        });
        btn_search_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ed_mStation_id = ed_mStation_routes.getText().toString();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ed_mStation_id", ed_mStation_id);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        // 해당 정류장을 지나가는 버스리스트를 로드한다
        String uri = "http://openapi.gbis.go.kr/ws/rest/busstationservice/route?serviceKey=" +
                "i%2FmgmkmoCSBv8EUR8Jv1%2FTOw767UUNZEI%2FSGQnCmnDSb4kM1Vty5Dsqlw%2Bcx%2B8o%2FtfNUzA7PNyaMnqVHCMqD8A%3D%3D&" +
                "stationId=" + StationId;
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList routeIdList = (NodeList) xpath.evaluate("//busRouteList/routeId", document, XPathConstants.NODESET);
            NodeList busNumberList = (NodeList) xpath.evaluate("//busRouteList/routeName", document, XPathConstants.NODESET);
            NodeList busTypeList = (NodeList) xpath.evaluate("//busRouteList/routeTypeName", document, XPathConstants.NODESET);

            for (int i = 0; i < routeIdList.getLength(); i++) {
                mRouteAdapter.addItem(
                        routeIdList.item(i).getTextContent(),
                        busNumberList.item(i).getTextContent(),
                        busTypeList.item(i).getTextContent()
                );
            }
            mListView_Route.setAdapter(mRouteAdapter);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }



        mListView_Route.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resultIntent = new Intent(getApplicationContext(), ResultActivity.class);
                resultIntent.putExtra("sel_stationId",StationId);
                resultIntent.putExtra("routeId", mRouteAdapter.getItem(position).routeId);
                resultIntent.putExtra("busNumber", mRouteAdapter.getItem(position).busNumber);
                startActivity(resultIntent);
            }
        });
    }
}
