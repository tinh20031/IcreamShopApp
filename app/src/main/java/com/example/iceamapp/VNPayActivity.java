package com.example.iceamapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class VNPayActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VNPayActivity.this.setContentView(R.layout.activity_vnpay);

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

                // Kiểm tra nếu URL là vnpay-return-for-app
                if (url.startsWith("https://10.0.2.2:7283/api/CartApi/vnpay-return-for-app")) {
                    Log.d("VNPayActivity", "Detected vnpay-return URL: " + url);
                    checkPaymentStatus(url);
                    return true; // Ngăn WebView tải URL này
                }

                view.loadUrl(url);
                return false;
            }

            @Override
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

            @Override
            public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler, android.net.http.SslError error) {
                // Bỏ qua lỗi SSL (chỉ dùng trong thử nghiệm)
                handler.proceed();
            }
        });

        if (orderUrl != null && !orderUrl.isEmpty()) {
            webView.loadUrl(orderUrl);
        } else {
            Toast.makeText(this, "Không thể tải trang thanh toán VNPAY!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void checkPaymentStatus(String url) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL apiUrl = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
                    conn.setRequestMethod("GET");

                    // Bỏ qua kiểm tra SSL (chỉ dùng trong thử nghiệm)
                    if (conn instanceof HttpsURLConnection) {
                        ((HttpsURLConnection) conn).setSSLSocketFactory(getUnsafeSSLSocketFactory());
                        ((HttpsURLConnection) conn).setHostnameVerifier((hostname, session) -> true);
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();
                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        Log.d("VNPayActivity", "Response from API: " + result);
                        JSONObject json = new JSONObject(result);
                        String status = json.getString("status");
                        String orderId = json.optString("orderId", String.valueOf(VNPayActivity.this.orderId));
                        String message = json.optString("message", "Không có thông báo");

                        // Tạo dialog với layout tùy chỉnh
                        AlertDialog.Builder builder = new AlertDialog.Builder(VNPayActivity.this);
                        View dialogView = getLayoutInflater().inflate(R.layout.layout_payment_dialog, null);
                        ImageView iconImageView = dialogView.findViewById(R.id.iconImageView);
                        TextView titleTextView = dialogView.findViewById(R.id.titleTextView);
                        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

                        // Áp dụng animation cho icon
                        Animation scaleAnimation = AnimationUtils.loadAnimation(VNPayActivity.this, R.anim.scale_animation);
                        iconImageView.startAnimation(scaleAnimation);

                        // Cấu hình nội dung dialog
                        if ("success".equals(status)) {
                            titleTextView.setText("Thành công!");
                            titleTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                            iconImageView.setImageResource(R.drawable.ic_check_circle);
                            messageTextView.setText(message + "\nMã đơn hàng: " + orderId);
                            confirmButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_dark)));
                        } else {
                            titleTextView.setText("Thất bại!");
                            titleTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            iconImageView.setImageResource(android.R.drawable.ic_delete);
                            messageTextView.setText(message + "\nMã đơn hàng: " + orderId);
                            confirmButton.setText("OK");
                            confirmButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        // Xử lý sự kiện nhấn nút Xác nhận
                        confirmButton.setOnClickListener(v -> {
                            Intent intent = new Intent(VNPayActivity.this, OrderHistoryActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        });

                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(VNPayActivity.this, "Lỗi xử lý phản hồi từ API", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(VNPayActivity.this, "Không thể kết nối đến API", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(url);
    }

    // Hàm bỏ qua kiểm tra SSL (chỉ dùng trong thử nghiệm)
    private javax.net.ssl.SSLSocketFactory getUnsafeSSLSocketFactory() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    @Override
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        return sslContext.getSocketFactory();
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