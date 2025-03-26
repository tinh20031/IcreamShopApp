package com.example.iceamapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.Services.CartApiService;
import com.example.iceamapp.adapter.CartAdapter;
import com.example.iceamapp.entity.Cart;
import com.example.iceamapp.entity.CreatePaymentRequest;
import com.example.iceamapp.entity.PaymentResponse;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectAddressActivity extends AppCompatActivity {
    private EditText editTextStreet;
    private MapView mapView;
    private Marker selectedMarker;
    private RecyclerView recyclerViewCartItems;
    private Button btnConfirm;
    private ProgressBar progressBar;
    private List<Cart> cartItems = new ArrayList<>();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "SelectAddressActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        // Khởi tạo cấu hình osmdroid
        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(getCacheDir(), "osmdroid/tiles"));
        Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));

        // Khởi tạo các view
        editTextStreet = findViewById(R.id.editTextStreet);
        mapView = findViewById(R.id.mapView);
        recyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
        btnConfirm = findViewById(R.id.btnConfirm);
        progressBar = findViewById(R.id.progressBar);

        // Nhận danh sách sản phẩm trong giỏ hàng từ CartActivity
        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Hiển thị danh sách sản phẩm trong giỏ hàng trong RecyclerView
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter cartAdapter = new CartAdapter(cartItems);
        recyclerViewCartItems.setAdapter(cartAdapter);

        // Yêu cầu quyền truy cập vị trí
        requestLocationPermission();

        // Khởi tạo bản đồ
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(10.7769, 106.7009); // Vị trí mặc định (TP. Hồ Chí Minh)
        mapView.getController().setCenter(startPoint);

        // Thêm marker khi người dùng nhấn vào bản đồ
        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                GeoPoint point = (GeoPoint) mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                if (selectedMarker != null) {
                    selectedMarker.remove(mapView);
                }
                selectedMarker = new Marker(mapView);
                selectedMarker.setPosition(point);
                selectedMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mapView.getOverlays().add(selectedMarker);
                mapView.invalidate();

                // Thực hiện geocoding trong một luồng nền
                new Thread(() -> {
                    try {
                        Geocoder geocoder = new Geocoder(SelectAddressActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
                        runOnUiThread(() -> {
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                String street = address.getAddressLine(0) != null ? address.getAddressLine(0) : "";
                                editTextStreet.setText(street);
                            } else {
                                Toast.makeText(SelectAddressActivity.this, "Không thể lấy địa chỉ từ tọa độ!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        runOnUiThread(() -> {
                            Log.e(TAG, "Lỗi Geocoder: " + e.getMessage(), e);
                            Toast.makeText(SelectAddressActivity.this, "Không thể lấy địa chỉ từ tọa độ!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
                return true; // Tiêu thụ sự kiện chạm
            }
            return false; // Không tiêu thụ các sự kiện chạm khác
        });

        // Xử lý khi nhấn nút xác nhận
        btnConfirm.setOnClickListener(v -> {
            final String street = editTextStreet.getText().toString().trim();

            if (selectedMarker == null) {
                Toast.makeText(this, "Vui lòng chọn địa chỉ trên bản đồ!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (street.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập địa chỉ!", Toast.LENGTH_SHORT).show();
                return;
            }

            GeoPoint position = selectedMarker.getPosition();
            progressBar.setVisibility(View.VISIBLE);
            new Thread(() -> {
                try {
                    Geocoder geocoder = new Geocoder(SelectAddressActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(position.getLatitude(), position.getLongitude(), 1);
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            Log.d(TAG, "Địa chỉ đầy đủ: " + address.getAddressLine(0));
                            Log.d(TAG, "Phường/Xã: " + address.getSubLocality());
                            Log.d(TAG, "Quận/Huyện: " + address.getLocality());
                            Log.d(TAG, "Tỉnh/Thành phố: " + address.getAdminArea());

                            String ward = address.getSubLocality() != null ? address.getSubLocality() : "";
                            String district = address.getLocality() != null ? address.getLocality() : "";
                            String province = address.getAdminArea() != null ? address.getAdminArea() : "";

                            Log.d(TAG, "Phường/Xã đã phân tích: " + ward);
                            Log.d(TAG, "Quận/Huyện đã phân tích: " + district);
                            Log.d(TAG, "Tỉnh/Thành phố đã phân tích: " + province);

                            proceedWithOrder(street, ward, district, province);
                        } else {
                            Toast.makeText(SelectAddressActivity.this, "Không thể lấy địa chỉ từ tọa độ!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "Lỗi Geocoder: " + e.getMessage(), e);
                        Toast.makeText(SelectAddressActivity.this, "Không thể lấy địa chỉ từ tọa độ!", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });
    }

    private void proceedWithOrder(String street, String ward, String district, String province) {
        // Kết hợp địa chỉ thành một chuỗi duy nhất
        String shippingAddress = street;
        if (!ward.isEmpty() && !street.contains(ward)) {
            shippingAddress += ", " + ward;
        }
        if (!district.isEmpty() && !street.contains(district)) {
            shippingAddress += ", " + district;
        }
        if (!province.isEmpty() && !street.contains(province)) {
            shippingAddress += ", " + province;
        }

        placeOrder(shippingAddress);
    }

    private void placeOrder(String shippingAddress) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra giỏ hàng trên client
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra giá và số lượng hợp lệ
        for (Cart cart : cartItems) {
            if (cart.getPrice() <= 0 || cart.getQuantity() <= 0) {
                Toast.makeText(this, "Có sản phẩm trong giỏ hàng có giá hoặc số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Kiểm tra tổng tiền
        double totalAmount = cartItems.stream()
                .mapToDouble(cart -> cart.getPrice() * cart.getQuantity())
                .sum() * 25000;
        if (totalAmount < 1000) {
            Toast.makeText(this, "Tổng số tiền phải lớn hơn 1,000 VND!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo CreatePaymentRequest
        CreatePaymentRequest requestBody = new CreatePaymentRequest(shippingAddress);

        Log.d(TAG, "Dữ liệu yêu cầu: " + new Gson().toJson(requestBody));

        CartApiService apiService = RetrofitClient.getCartApiService();
        progressBar.setVisibility(View.VISIBLE);

        // Gọi endpoint thanh toán VNPAY
        Call<PaymentResponse> call = apiService.createOrderWithPaymentMethod(userId, requestBody); // Sửa Call<JSONObject> thành Call<PaymentResponse>
        handleOrderResponse(call);
    }
    private void handleOrderResponse(Call<PaymentResponse> call) {
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    PaymentResponse paymentResponse = response.body();
                    String orderUrl = paymentResponse.getOrderUrl();
                    Log.d(TAG, "OrderUrl nhận được: " + orderUrl); // Thêm log để kiểm tra giá trị
                    if (orderUrl == null || orderUrl.isEmpty()) {
                        Log.e(TAG, "OrderUrl rỗng hoặc null");
                        Toast.makeText(SelectAddressActivity.this, "Không nhận được URL thanh toán từ server!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.d(TAG, "Nhận được OrderUrl: " + orderUrl);
                    Toast.makeText(SelectAddressActivity.this, "Đặt hàng thành công! Chuyển đến trang thanh toán VNPAY...", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SelectAddressActivity.this, VNPayActivity.class);
                    intent.putExtra("orderUrl", orderUrl);
                    startActivity(intent);
                } else {
                    try {
                        String errorMessage = "Lỗi khi đặt hàng: " + response.code() + " - " + response.message();
                        Log.e(TAG, errorMessage);
                        if (response.code() == 404) {
                            errorMessage = "Không tìm thấy dịch vụ đặt hàng trên server. Vui lòng kiểm tra kết nối hoặc liên hệ hỗ trợ.";
                        }
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Nội dung lỗi từ server: " + errorBody);
                            errorMessage += "\nChi tiết: " + errorBody;
                        }
                        Toast.makeText(SelectAddressActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e(TAG, "Lỗi khi phân tích nội dung lỗi: " + e.getMessage(), e);
                        Toast.makeText(SelectAddressActivity.this, "Lỗi khi đặt hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Lỗi API đặt hàng: " + t.getMessage(), t);
                Toast.makeText(SelectAddressActivity.this, "Lỗi khi đặt hàng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền truy cập vị trí đã được cấp!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
}