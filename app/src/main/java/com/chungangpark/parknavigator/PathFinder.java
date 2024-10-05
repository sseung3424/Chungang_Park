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
    private TextToSpeech textToSpeech;
    private static final double DESTINATION_THRESHOLD_DISTANCE = 20; // 목적지까지 남은 거리가 20미터 이하일 때 안내

    // 생성자
    public PathFinder(Context context, NaverMap naverMap) {
        this.naverMap = naverMap;
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

    // Polyline 상의 다음 지점을 가져오는 함수
    private LatLng getNextPointOnPolyline(LatLng userLocation, PolylineOverlay polyline) {
        List<LatLng> polylinePoints = polyline.getCoords();
        LatLng closestPoint = getClosestPointOnPolyline(userLocation, polyline);
        int closestIndex = polylinePoints.indexOf(closestPoint);

        // 다음 지점이 있으면 반환
        if (closestIndex != -1 && closestIndex < polylinePoints.size() - 1) {
            return polylinePoints.get(closestIndex + 1);
        }
        return null;
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
    public void navigateAlongPolyline(LatLng userLocation, PolylineOverlay polyline, LatLng destination) {
        List<LatLng> polylinePoints = polyline.getCoords();
        LatLng closestPoint = getClosestPointOnPolyline(userLocation, polyline);
        LatLng nextPoint = getNextPointOnPolyline(userLocation, polyline);

        double distanceToDestination = getDistance(userLocation, destination);

        if (distanceToDestination <= DESTINATION_THRESHOLD_DISTANCE) {
            // 목적지까지의 거리가 가까워지면 마지막 안내
            navigateToDestination(userLocation, destination);
        } else if (closestPoint != null && nextPoint != null) {
            // 사용자가 이동할 방향 계산 (다음 점자블록으로)
            double bearingToNextPoint = calculateBearing(closestPoint, nextPoint);
            double userBearing = naverMap.getLocationOverlay().getBearing(); // 사용자가 보고 있는 방향

            double direction = bearingToNextPoint - userBearing;
            if (direction < 0) {
                direction += 360; // 방향 각도는 0 ~ 360 사이여야 함
            }

            Log.d("Direction", "Calculated direction: " + direction); // 로그로 확인

            String directionMessage = getDirectionMessage(direction); // 방향 안내 메시지 생성
            speakDirection(directionMessage);

            // 다음 점자블록까지의 거리 안내
            double distanceToNextBlock = getDistance(closestPoint, nextPoint);
            speakDirection("다음 점자블록까지의 거리는 약 " + Math.round(distanceToNextBlock) + "미터입니다.");

            // Polyline 상의 가장 가까운 지점으로 카메라 이동
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(nextPoint).zoomTo(17);
            naverMap.moveCamera(cameraUpdate);

        } else {
            speakDirection("경로를 찾을 수 없습니다.");
        }
    }

    // 사용자 위치에서 목적지로 향하는 방향(각도) 계산
    private double calculateBearing(LatLng start, LatLng end) {
        double startLat = Math.toRadians(start.latitude);
        double startLng = Math.toRadians(start.longitude);
        double endLat = Math.toRadians(end.latitude);
        double endLng = Math.toRadians(end.longitude);

        double dLng = endLng - startLng;
        double x = Math.sin(dLng) * Math.cos(endLat);
        double y = Math.cos(startLat) * Math.sin(endLat) - Math.sin(startLat) * Math.cos(endLat) * Math.cos(dLng);

        return (Math.toDegrees(Math.atan2(x, y)) + 360) % 360; // 북쪽을 기준으로 각도 반환
    }

    private boolean isTTSInitialized = false;

    // TextToSpeech 초기화
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.KOREAN);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "해당 언어는 지원되지 않습니다.");
            } else {
                isTTSInitialized = true; // TTS 초기화 성공 플래그 설정
            }
        } else {
            Log.e("TTS", "TTS 초기화 실패.");
        }
    }

    // 현재 위치에서 목적지로 안내하는 함수
    public void navigateToDestination(LatLng userLocation, LatLng destination) {
        double bearingToDestination = calculateBearing(userLocation, destination); // 목적지 방향
        double userBearing = naverMap.getLocationOverlay().getBearing(); // 사용자가 보고 있는 방향

        double direction = bearingToDestination - userBearing;

        if (direction < 0) {
            direction += 360; // 방향 각도는 0 ~ 360 사이여야 함
        }

        Log.d("Direction", "Calculated direction: " + direction); // 로그로 확인

        String directionMessage = getDirectionMessage(direction); // 방향 안내 메시지 생성
        Log.d("DirectionMessage", "Direction message: " + directionMessage); // 로그로 메시지 확인

        // 음성 안내
        speakDirection(directionMessage);

        // 목적지까지의 거리 계산
        double distance = getDistance(userLocation, destination);
        speakDirection("목적지까지의 거리는 약 " + Math.round(distance) + "미터입니다.");
    }

    // 이동해야 할 방향에 대한 메시지 생성
    private String getDirectionMessage(double direction) {
        if (direction >= 0 && direction < 45) {
            return "직진하세요.";
        } else if (direction >= 45 && direction < 135) {
            return "오른쪽으로 이동하세요.";
        } else if (direction >= 135 && direction < 225) {
            return "뒤로 돌아가세요.";
        } else if (direction >= 225 && direction < 315) {
            return "왼쪽으로 이동하세요.";
        } else {
            return "직진하세요.";
        }
    }

    // 음성 안내 함수 - 큐에 쌓아서 순차적으로 안내
    private void speakDirection(String message) {
        if (isTTSInitialized) {
            textToSpeech.speak(message, TextToSpeech.QUEUE_ADD, null, null); // 이전 메시지가 끝난 후 실행
        } else {
            Log.e("TTS", "TTS가 초기화되지 않았습니다.");
        }
    }

    // TextToSpeech 종료
    public void shutdownTTS() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
