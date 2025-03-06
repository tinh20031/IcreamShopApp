package com.example.iceamapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.iceamapp.R;

public class HeaderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        // Tìm các ImageView
        ImageView cartIcon = view.findViewById(R.id.cartIcon);
        ImageView notificationIcon = view.findViewById(R.id.notificationIcon);
        ImageView userAvatar = view.findViewById(R.id.userAvatar);

        // Xử lý sự kiện nhấp
//        cartIcon.setOnClickListener(v -> loadContentFragment(new CartFragment()));
//        notificationIcon.setOnClickListener(v -> loadContentFragment(new NotificationFragment()));
//        userAvatar.setOnClickListener(v -> loadContentFragment(new UserFragment()));

        return view;
    }

    // Hàm thay thế Fragment nội dung
//    private void loadContentFragment(Fragment fragment) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.contentFragmentContainer, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}