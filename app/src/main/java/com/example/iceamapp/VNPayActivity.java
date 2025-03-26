package com.example.iceamapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iceamapp.OrderHistoryActivity;
import com.example.iceamapp.R;

public class VNPayActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnpay);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        orderId = getIntent().getIntExtra("orderId", -1);
        String orderUrl = getIntent().getStringExtra("orderUrl");

        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportMultipleWindows(false);

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("VNPayActivity", "URL loaded: " + url);
                if (url.startsWith("iceamapp://payment-callback")) {
                    Log.d("VNPayActivity", "Deep Link received: " + url);
                    // Handle payment result
                    if (url.contains("vnp_ResponseCode=00")) {
                        Toast.makeText(VNPayActivity.this, "Thanh toán VNPAY thành công!", Toast.LENGTH_LONG).show();
                    } else {
                        String responseCode = "Unknown";
                        if (url.contains("vnp_ResponseCode")) {
                            String[] parts = url.split("vnp_ResponseCode=");
                            if (parts.length > 1) {
                                String codePart = parts[1];
                                int ampersandIndex = codePart.indexOf("&");
                                responseCode = ampersandIndex != -1 ? codePart.substring(0, ampersandIndex) : codePart;
                            }
                        }
                        Toast.makeText(VNPayActivity.this, "Thanh toán VNPAY thất bại! Mã lỗi: " + responseCode, Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent(VNPayActivity.this, OrderHistoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                    return true;
                }
                view.loadUrl(url);
                return false;
            }            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(VNPayActivity.this, "Lỗi khi tải trang thanh toán: " + description, Toast.LENGTH_SHORT).show();
            }
        });

        if (orderUrl != null && !orderUrl.isEmpty()) {
            webView.loadUrl(orderUrl);
        } else {
            Toast.makeText(this, "Không thể tải trang thanh toán VNPAY!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}