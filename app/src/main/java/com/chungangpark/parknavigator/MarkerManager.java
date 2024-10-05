// MarkerManager.java
package com.chungangpark.parknavigator;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.Arrays;
import java.util.List;

public class MarkerManager {
    private NaverMap naverMap;

    public MarkerManager(NaverMap naverMap) {
        this.naverMap = naverMap;
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
                new LatLng(37.53481081, 126.91558614)
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
        addMarkerList(toilet, R.drawable.toliet_marker, 150, 150); // 크기 조절 예시
        addMarkerList(information, R.drawable.information_marker, 150, 150); // 크기 조절 예시
        addMarkerList(store, R.drawable.information_marker, 150, 150); // 크기 조절 예시
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
        }

    }
}