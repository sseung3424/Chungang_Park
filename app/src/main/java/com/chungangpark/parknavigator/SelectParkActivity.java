package com.chungangpark.parknavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;

public class SelectParkActivity extends AppCompatActivity {
    private NaverMap naverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_park);

        // 여의도 버튼 (LinearLayout으로 변경)
        LinearLayout btnYeouido = findViewById(R.id.btn_yeouido);
        btnYeouido.setOnClickListener(v -> {
            // MainActivity로 이동하고 선택한 공원 정보 전달
            Intent intent = new Intent(SelectParkActivity.this, MainActivity.class);
            intent.putExtra("park_name", "여의도");
            startActivity(intent);
        });

        // 잠실 버튼 (LinearLayout으로 변경)
        LinearLayout btnJamsil = findViewById(R.id.btn_jamsil);
        btnJamsil.setOnClickListener(v -> {
            // MainActivity로 이동하고 선택한 공원 정보 전달
            Intent intent = new Intent(SelectParkActivity.this, MainActivity.class);
            intent.putExtra("park_name", "잠실");
            startActivity(intent);
        });

        // 망원 버튼 (LinearLayout으로 변경)
        LinearLayout btnMangwon = findViewById(R.id.btn_mangwon);
        btnMangwon.setOnClickListener(v -> {
            // MainActivity로 이동하고 선택한 공원 정보 전달
            Intent intent = new Intent(SelectParkActivity.this, MainActivity.class);
            intent.putExtra("park_name", "망원");
            startActivity(intent);
        });
    }
}
