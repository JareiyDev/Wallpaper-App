package com.phbet.wallpaper;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WebviewActivity extends AppCompatActivity {
    private WebView webview;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        webview = findViewById(R.id.webview);
        Bundle b = getIntent().getExtras();
        link = b.getString("link");
        webview = findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.postDelayed(() -> webview.loadUrl(link), 500);

        //Thực hiện action khi trang hoàn toàn tải
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //Thực hiện action
            }
        });
    }
}