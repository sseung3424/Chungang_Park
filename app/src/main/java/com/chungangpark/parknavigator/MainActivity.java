package com.chungangpark.parknavigator;

// 점형 점자블록 추가
import com.naver.maps.map.overlay.Marker;
// 선형 점자 블록 추가
import com.naver.maps.map.overlay.PolylineOverlay;
// latlng 클래스 임포트
import com.naver.maps.geometry.LatLng;

// 블루투스 관련 라이브러리 임포트 확인
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast; // Toast 추가
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList; // ArrayList 추가
import java.util.List; // List 추가
import java.util.UUID; // UUID 추가

import java.util.Arrays;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.CameraUpdateParams;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;

import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private Animator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SelectParkActivity spa = new SelectParkActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 선택된 공원의 이름을 Intent로 전달받음
        String parkName = getIntent().getStringExtra("park_name");

        // 한강 공원 목록 버튼 설정
        Button selectParkButton = findViewById(R.id.select_park_button);
        selectParkButton.setOnClickListener(v -> spa.showParkListDialog());

        // ActionBar 설정 (위치 추적 모드를 위한)
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // FusedLocationSource 설정 (위치 권한 요청)
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // MapFragment 가져오기
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(new NaverMapOptions().locationButtonEnabled(true));
            getSupportFragmentManager().beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        // 지도 초기화
        mapFragment.getMapAsync(this);

        /*// 블루투스 설정 함수 호출
        setupBluetooth();*/
    }

    // 클래스에 점자블록 리스트 추가
    public static List<PolylineOverlay> brailleBlocks = new ArrayList<>();

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        SelectParkActivity spa = new SelectParkActivity();
        ArduinoVibrationController avc = new ArduinoVibrationController();
        BrailleBlockDetector bbd = new BrailleBlockDetector();
        // 네이버 지도 객체 설정
        this.naverMap = naverMap;

        // 지도 UI 설정 (줌 버튼)
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(false);

        // 위치 오버레이 설정
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setSubIcon(LocationOverlay.DEFAULT_SUB_ICON_ARROW);
        locationOverlay.setCircleOutlineWidth(0); // 원 테두리 제거

        // 위치 추적 설정
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        // 위치 추적 모드를 None으로 설정 (원하는 경우 Follow로 변경 가능)
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);

        // 선택한 공원으로 지도 이동
        String parkName = getIntent().getStringExtra("park_name");
        if (parkName != null) {
            spa.moveToSelectedPark(parkName);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}