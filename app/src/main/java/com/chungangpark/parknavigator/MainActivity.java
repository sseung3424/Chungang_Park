package com.chungangpark.parknavigator;

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
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private Animator animator;


    private Button selectParkButton;

    // 한강 공원의 위치를 정의합니다.
    private static final LatLng YEUIDO_PARK = new LatLng(37.5283169, 126.9328034); // 여의도 한강 공원 좌표
    private static final LatLng MANGWON_PARK = new LatLng(37.5580, 126.9027);
    private static final LatLng JAMSIL_PARK = new LatLng(37.5100, 127.1000); // 잠실 한강 공원 좌표

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 선택된 공원의 이름을 Intent로 전달받음
        String parkName = getIntent().getStringExtra("park_name");

        // 한강 공원 목록 버튼 설정
        selectParkButton = findViewById(R.id.select_park_button);
        selectParkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParkListDialog();
            }
        });

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
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
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

        // 선택한 공원으로 지도 이동
        String parkName = getIntent().getStringExtra("park_name");
        if (parkName != null) {
            moveToSelectedPark(parkName);
        }
    }
    // 한강 공원 목록 다이얼로그 표시
    private void showParkListDialog() {
        final String[] parkList = {"여의도 한강 공원", "망원 한강 공원","잠실 한강 공원"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("한강 공원 선택")
                .setItems(parkList, (dialog, which) -> {
                    switch (which) {
                        case 0: // 여의도 한강 공원 선택
                            moveToPark(YEUIDO_PARK);
                            break;
                        case 1: // 망원 한강 공원 선택
                            moveToPark(MANGWON_PARK);
                            break;
                        case 2: // 망원 한강 공원 선택
                            moveToPark(JAMSIL_PARK);
                            break;
                    }
                })

                .show();
    }

    // 공원의 이름에 따라 올바른 LatLng 좌표를 전달하는 메서드
    private void moveToSelectedPark(String parkName) {
        LatLng parkLocation = null;

        switch (parkName) {
            case "여의도":
                parkLocation = YEUIDO_PARK;
                break;
            case "망원":
                parkLocation = MANGWON_PARK;
                break;
            case "잠실":
                parkLocation = JAMSIL_PARK;
                break;
            default:
                Toast.makeText(this, "알 수 없는 공원 선택", Toast.LENGTH_SHORT).show();
                return;
        }

        if (parkLocation != null) {
            moveToPark(parkLocation);  // LatLng 좌표를 사용하여 공원으로 이동
        }
    }

    // 선택한 공원으로 지도 이동
    private void moveToPark(LatLng parkLocation) {

        // 줌 레벨을 12로 설정하여 더 넓게 보기
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(parkLocation).zoomTo(15);
        naverMap.moveCamera(cameraUpdate);

        // 지도 반경을 약 1km로 확장
        double latitude = parkLocation.latitude;
        double longitude = parkLocation.longitude;

        double radiusInDegrees = 0.009; // 반경 약 1km
        LatLng southwest = new LatLng(latitude - radiusInDegrees, longitude - radiusInDegrees);
        LatLng northeast = new LatLng(latitude + radiusInDegrees, longitude + radiusInDegrees);

        // setExtent()을 사용하여 지도 경계 설정
        naverMap.setExtent(new com.naver.maps.geometry.LatLngBounds(southwest, northeast));

        Toast.makeText(this, "공원이동: " + parkLocation.latitude + ", " + parkLocation.longitude, Toast.LENGTH_SHORT).show();
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
