package com.chungangpark.parknavigator;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.PolylineOverlay;

import java.util.List;
import java.util.Locale;

public class PathFinder implements TextToSpeech.OnInitListener {
    private Context context;
    private NaverMap naverMap;
    private String apiKeyId;
    private String apiKeySecret;
    private TextToSpeech textToSpeech;

    // 생성자
    public PathFinder(Context context, NaverMap naverMap, String apiKeyId, String apiKeySecret) {
        this.context = context;
        this.naverMap = naverMap;
        this.apiKeyId = apiKeyId;
        this.apiKeySecret = apiKeySecret;
        this.textToSpeech = new TextToSpeech(context, this); // TTS 초기화
    }

    // 사용자 위치와 Polyline 간의 가장 가까운 점 찾기
    public LatLng getClosestPointOnPolyline(LatLng userLocation, PolylineOverlay polyline) {
        List<LatLng> polylinePoints = polyline.getCoords();
        LatLng closestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (LatLng point : polylinePoints) {
            double distance = getDistance(userLocation, point);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }
        return closestPoint;
    }

    // 두 좌표 간의 거리 계산 함수
    private double getDistance(LatLng point1, LatLng point2) {
        double earthRadius = 6371e3; // 지구 반지름, 미터
        double lat1 = Math.toRadians(point1.latitude);
        double lat2 = Math.toRadians(point2.latitude);
        double deltaLat = Math.toRadians(point2.latitude - point1.latitude);
        double deltaLng = Math.toRadians(point2.longitude - point1.longitude);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    // 사용자 위치에서 Polyline을 따라가는 경로 찾기
    public void navigateAlongPolyline(LatLng userLocation, PolylineOverlay polyline) {
        LatLng closestPoint = getClosestPointOnPolyline(userLocation, polyline);

        if (closestPoint != null) {
            // Polyline 상의 가장 가까운 지점으로 카메라 이동
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(closestPoint).zoomTo(17);
            naverMap.moveCamera(cameraUpdate);

            // 음성 안내 추가
            String directionMessage = "경로를 따라 이동 중입니다. 현재 위치는 " +
                    "위도 " + closestPoint.latitude + ", 경도 " + closestPoint.longitude + "입니다.";
            speakDirection(directionMessage);
        } else {
            speakDirection("경로를 찾을 수 없습니다.");
        }
    }

    // TextToSpeech 초기화
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // 언어 설정 (한국어 또는 원하는 언어로 설정 가능)
            int result = textToSpeech.setLanguage(Locale.KOREAN);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "해당 언어는 지원되지 않습니다.");
            }
        } else {
            Log.e("TTS", "TTS 초기화 실패.");
        }
    }

    // 음성 안내 함수
    private void speakDirection(String message) {
        if (textToSpeech != null) {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // 경로 찾기 실행
    public void findPath(LatLng startLatLng, LatLng endLatLng) {
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(startLatLng).zoomTo(15);
        naverMap.moveCamera(cameraUpdate);

        String pathMessage = "출발지에서 경로를 따라 잠실 공원까지 이동합니다.";
        speakDirection(pathMessage);

        // 실제 Polyline을 사용하여 경로 안내 로직을 추가할 수 있음
    }

    // TextToSpeech 종료
    public void shutdownTTS() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
