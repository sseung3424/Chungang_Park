package com.chungangpark.parknavigator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import com.naver.maps.geometry.LatLng;   // LatLng 클래스 임포트
import com.naver.maps.map.overlay.Marker; // Marker 클래스 임포트
import com.naver.maps.map.overlay.OverlayImage; // Marker 아이콘 관련

import java.util.List;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private Animator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void onMapReady(@NonNull NaverMap naverMap) {
        // 네이버 지도 객체 설정
        this.naverMap = naverMap;

        // 지도 UI 설정 (줌 버튼)
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(true);

        // 위치 오버레이 설정
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setSubIcon(LocationOverlay.DEFAULT_SUB_ICON_ARROW);
        locationOverlay.setCircleOutlineWidth(0); // 원 테두리 제거

        // 위치 추적 설정
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Face);

        // toilet_list
        List<LatLng> toilet = Arrays.asList(
                // 아래는 예시 위치 -> 화장실 좌표 추가해야함!!!!!
                new LatLng(37.5666102, 126.9783881), // 서울시청
                new LatLng(37.570041, 126.982794),   // 경복궁
                new LatLng(37.551229, 126.988205)    // 남산타워
        );
        // information_list
        List<LatLng> information = Arrays.asList(
                // 아래는 예시 위치 -> 안내소 좌표 추가해야함!!!!!
                new LatLng(37.5666102, 126.9783881), // 서울시청
                new LatLng(37.570041, 126.982794),   // 경복궁
                new LatLng(37.551229, 126.988205)    // 남산타워
        );
        // store_list
        List<LatLng> store = Arrays.asList(
                // 아래는 예시 위치 -> 매점 좌표 추가해야함!!!!!
                new LatLng(37.5666102, 126.9783881), // 서울시청
                new LatLng(37.570041, 126.982794),   // 경복궁
                new LatLng(37.551229, 126.988205)    // 남산타워
        );
        // 아래에 다른 위치 추가 필요

        // toilet_marker
        for (LatLng place : toilet) {
            Marker marker = new Marker();
            marker.setPosition(place); // 심볼 위치 설정
            marker.setMap(naverMap);   // 지도에 마커 추가
            marker.setIcon(OverlayImage.fromResource(R.drawable.custom_marker)); // 커스텀 마커 이미지 설정 가능
        }
        // information_marker
        for (LatLng place : information) {
            Marker marker = new Marker();
            marker.setPosition(place); // 심볼 위치 설정
            marker.setMap(naverMap);   // 지도에 마커 추가
            marker.setIcon(OverlayImage.fromResource(R.drawable.custom_marker)); // 커스텀 마커 이미지 설정 가능
        }
        // store_marker
        for (LatLng place : store) {
            Marker marker = new Marker();
            marker.setPosition(place); // 심볼 위치 설정
            marker.setMap(naverMap);   // 지도에 마커 추가
            marker.setIcon(OverlayImage.fromResource(R.drawable.custom_marker)); // 커스텀 마커 이미지 설정 가능
        }

        // Marker는 심볼처럼 기본적으로 지도에 떠 있기 때문에 추가 설정 없이도 지도에 표시됨.
    }
    // 아래에 마커 추가 필요

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
