package com.example.ryan.xmlparsing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

public class CustomWebView extends AppCompatActivity {
    ImageButton btn_close_GBIS;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_web_view);

        // 첫 화면에서 사용자가 www.gbis.go.kr 사이트로 이동하여 자신이 대기하는 정류장번호를 알기위한 웹뷰 로직 처리작업.
        btn_close_GBIS = findViewById(R.id.close_GBIS);
        webView = findViewById(R.id.GBISWebView);
        btn_close_GBIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.loadUrl("http://www.gbis.go.kr");
    }
}
