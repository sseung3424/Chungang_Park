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
    private SeekBar seekBarBearing;
    private TextView valueBearing;

    // 한강 공원의 위치를 정의합니다.
    private static final LatLng YEUIDO_PARK = new LatLng(37.5283169, 126.9328034); // 여의도 한강 공원 좌표
    private static final LatLng BANPO_PARK = new LatLng(37.5088, 126.9920);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        // 각도 조절 SeekBar 설정
        seekBarBearing = findViewById(R.id.seek_bar_bearing);
        valueBearing = findViewById(R.id.value_bearing);
        seekBarBearing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (naverMap != null) {
                    // 지도 줌 레벨을 설정하는 코드
                    double zoomLevel = (double) progress / 10; // 줌 레벨을 0.1 단위로 설정
                    CameraUpdate cameraUpdate = CameraUpdate.zoomTo(zoomLevel);
                    naverMap.moveCamera(cameraUpdate);

                    valueBearing.setText(String.format("줌 레벨: %.1f", zoomLevel)); // 줌 레벨 표시
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
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
        naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
    }
    // 한강 공원 목록 다이얼로그 표시
    private void showParkListDialog() {
        final String[] parkList = {"여의도 한강 공원", "반포 한강 공원"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("한강 공원 선택")
                .setItems(parkList, (dialog, which) -> {
                    switch (which) {
                        case 0: // 여의도 한강 공원 선택
                            moveToPark(YEUIDO_PARK);
                            break;
                        case 1: // 반포 한강 공원 선택
                            moveToPark(BANPO_PARK);
                            break;
                    }
                })

                .show();
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
    // 원 애니메이션
    private void animateCircle(@NonNull LocationOverlay locationOverlay) {
        if (animator != null) {
            animator.cancel();
        }

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator radiusAnimator = ObjectAnimator.ofInt(locationOverlay, "circleRadius",
                0, getResources().getDimensionPixelSize(R.dimen.location_overlay_circle_raduis));
        radiusAnimator.setRepeatCount(2);

        ObjectAnimator colorAnimator = ObjectAnimator.ofInt(locationOverlay, "circleColor",
                Color.argb(127, 148, 186, 250), Color.argb(0, 148, 186, 250));
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setRepeatCount(2);

        animatorSet.setDuration(1000);
        animatorSet.playTogether(radiusAnimator, colorAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                locationOverlay.setCircleRadius(0);
            }
        });
        animatorSet.start();

        animator = animatorSet;
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
