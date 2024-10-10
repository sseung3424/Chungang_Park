// MarkerManager.java
package com.chungangpark.parknavigator;

import android.location.Location;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkerManager {
    private NaverMap naverMap;
    private List<Marker> markers = new ArrayList<>();
    private List<Marker> toiletMarkers = new ArrayList<>();
    private List<Marker> informationMarkers = new ArrayList<>();
    private List<Marker> storeMarkers = new ArrayList<>();

    public MarkerManager(NaverMap naverMap) {
        this.naverMap = naverMap;
    }

    private void addMarkerList(List<LatLng> places, int resource, int width, int height) {
        for (LatLng place : places) {
            Marker marker = new Marker();
            marker.setPosition(place);
            marker.setMap(naverMap);
            marker.setIcon(OverlayImage.fromResource(resource));

            // 마커 크기 조정
            marker.setWidth(width);   // 너비 설정
            marker.setHeight(height); // 높이 설정

            // 리스트에 추가
            markers.add(marker); // 추가된 마커를 리스트에 저장
        }
    }

    // 모든 마커를 반환하는 메서드
    public List<Marker> getAllMarkers() {
        return markers;
    }

    public void addMarkers() {
        // toilet_list
        List<LatLng> toilet = Arrays.asList(
                new LatLng(37.51925551, 126.94159282),
                new LatLng(37.52172615, 126.94154885),
                new LatLng(37.52332775, 126.93968532),
                new LatLng(37.52495857, 126.93753876),
                new LatLng(37.52509346, 126.93766530),
                new LatLng(37.52747783, 126.93446962),
                new LatLng(37.52739352, 126.93453164),
                new LatLng(37.52875338, 126.93157210),
                new LatLng(37.52863411, 126.93165821),
                new LatLng(37.53052242, 126.92955526),
                new LatLng(37.53065916, 126.92976968),
                new LatLng(37.53069266, 126.92753863),
                new LatLng(37.53210475, 126.92640052),
                new LatLng(37.53312605, 126.92299011),
                new LatLng(37.53429215, 126.91852670),
                new LatLng(37.53481081, 126.91558614),
                new LatLng(37.58494730, 126.88573480)
        );

        // information_list
        List<LatLng> information = Arrays.asList(
                new LatLng(37.52635436, 126.93357377) // 안내소 위치
        );

        // store_list
        List<LatLng> store = Arrays.asList(
                new LatLng(37.52276599, 126.94165806), // 이마트 24
                new LatLng(37.52308459, 126.94002640),   // 세븐일레븐
                new LatLng(37.52498435, 126.93899395) ,    // CU
                new LatLng(37.52576872, 126.93842445)  ,  // CU
                new LatLng(37.52707631, 126.93284706)  ,  // 세븐일레븐
                new LatLng(37.52822530, 126.93158868) ,   // 세븐일레븐
                new LatLng(37.53125275, 126.92867288) , // 이마트 24
                new LatLng(37.53084445, 126.92775391) , // 씨스페이스
                new LatLng(37.53125275, 126.92867288) , // 이마트 24
                new LatLng(37.53324449, 126.92329270)  // 이마트 24
        );

        // 마커 추가
        addMarkerList(toilet, R.drawable.toliet_marker, 150, 150, toiletMarkers);
        addMarkerList(information, R.drawable.information_marker, 150, 150, informationMarkers);
        addMarkerList(store, R.drawable.store_marker, 150, 150, storeMarkers);
    }

    private void addMarkerList(List<LatLng> places, int resource, int width, int height, List<Marker> markerList) {
        for (LatLng place : places) {
            Marker marker = new Marker();
            marker.setPosition(place);
            marker.setMap(naverMap);
            marker.setIcon(OverlayImage.fromResource(resource));
            marker.setWidth(width);
            marker.setHeight(height);
            markerList.add(marker);
            markers.add(marker);
        }
    }


    // 사용자 위치와 30m 반경 내에 있는 마커의 정보를 반환
    public List<String> getNearbyMarkers(LatLng userLocation, double radiusMeters) {
        List<String> nearbyMarkers = new ArrayList<>();

        for (Marker marker : markers) {
            double distance = getDistance(userLocation, marker.getPosition());
            if (distance <= radiusMeters) {
                nearbyMarkers.add("위치: " + marker.getPosition().latitude + ", " + marker.getPosition().longitude);
            }
        }

        return nearbyMarkers;
    }

    // 두 지점 간의 거리 계산 (미터 단위)
    private double getDistance(LatLng point1, LatLng point2) {
        float[] results = new float[1];
        Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, results);
        return results[0]; // 미터 단위로 반환
    }

    // 특정 마커 종류별로 반환하는 메서드
    public List<Marker> getToiletMarkers() {
        return toiletMarkers;
    }

    public List<Marker> getInformationMarkers() {
        return informationMarkers;
    }

    public List<Marker> getStoreMarkers() {
        return storeMarkers;
    }
}