package com.example.iceamapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.iceamapp.R;
import com.example.iceamapp.CartActivity;

public class HeaderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        // Tìm các ImageView
        ImageView cartIcon = view.findViewById(R.id.cartIcon);
        ImageView notificationIcon = view.findViewById(R.id.notificationIcon);
        ImageView userAvatar = view.findViewById(R.id.userAvatar);

        // Xử lý sự kiện nhấp vào giỏ hàng (Mở Activity)
        cartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nhấp vào thông báo (Chuyển Fragment)
        notificationIcon.setOnClickListener(v -> replaceFragment(new NotificationFragment()));

        // Xử lý sự kiện nhấp vào Avatar (Chuyển Fragment)
        userAvatar.setOnClickListener(v -> replaceFragment(new UserFragment()));

        return view;
    }

    // Hàm thay thế Fragment trong MainActivity
    private void replaceFragment(Fragment fragment) {
        if (getActivity() == null || getActivity().getSupportFragmentManager() == null) return; // Kiểm tra xem Activity có tồn tại không

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment); // Đảm bảo ID này khớp với nơi chứa Fragment trong MainActivity
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
