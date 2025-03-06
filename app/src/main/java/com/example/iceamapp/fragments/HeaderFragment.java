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
import com.example.iceamapp.fragments.CartActivity;
import com.example.iceamapp.R;

public class HeaderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        // Xử lý khi nhấn vào giỏ hàng
        ImageView cartIcon = view.findViewById(R.id.cartIcon);
        cartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
