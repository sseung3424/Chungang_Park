package com.chungangpark.parknavigator;

import androidx.annotation.NonNull;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.CircleOverlay;
import android.content.Context;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class BrailleBlockManager {
    private Context context;
    private List<LatLng> brailleBlockPoints;
    private boolean isUserNearBrailleBlock = false; // 사용자 상태를 추적하는 플래그

    public BrailleBlockManager(Context context) {
        this.context = context;
        brailleBlockPoints = new ArrayList<>();

        // 세 좌표를 점자블록 리스트에 추가
        brailleBlockPoints.add(new LatLng(37.51999291, 127.09851956));
        brailleBlockPoints.add(new LatLng(37.52000368, 127.09853365));
        brailleBlockPoints.add(new LatLng(37.52001018, 127.09854337));
    }

    // 지도에 점자블록 좌표 추가하는 함수 (별도의 함수 호출 없이 직접 추가)
    public void addBrailleBlockOnMap(@NonNull NaverMap naverMap) {
        for (LatLng point : brailleBlockPoints) {
            CircleOverlay circle = new CircleOverlay();
            circle.setCenter(point);
            circle.setRadius(1.0); // 반경 1m
            circle.setColor(0x40FFA500); // 주황색 반투명
            circle.setOutlineColor(0xFFFFA500); // 주황색 테두리
            circle.setOutlineWidth(3); // 테두리 두께
            circle.setMap(naverMap); // 지도에 원 추가
        }

        // 사용자 위치 변화 리스너 추가
        naverMap.addOnLocationChangeListener(location -> {
            LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());
            checkUserProximity(userPosition);  // 사용자와 좌표 간의 거리 계산 및 알림
        });
    }

    // 사용자와 점자블록 좌표 간의 거리를 계산하는 함수
    private double distanceBetween(LatLng start, LatLng end) {
        double earthRadius = 6371000; // 지구 반지름 (미터)
        double dLat = Math.toRadians(end.latitude - start.latitude);
        double dLng = Math.toRadians(end.longitude - start.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(start.latitude)) * Math.cos(Math.toRadians(end.latitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c; // 두 점 사이의 거리 반환 (미터)
    }

    // 사용자와 세 좌표 간의 최소 거리를 계산하고 알림을 보내는 함수
    private void checkUserProximity(LatLng userPosition) {
        double minDistance = Double.MAX_VALUE;

        // 사용자와 각 점자블록 좌표 간의 거리를 계산
        for (LatLng point : brailleBlockPoints) {
            double distance = distanceBetween(userPosition, point);
            if (distance < minDistance) {
                minDistance = distance; // 최소 거리 업데이트
            }
        }

        // 최소 거리가 2m 이내일 때 Toast 신호가 계속 발생하도록 처리
        if (minDistance <= 2.0) {
            if (!isUserNearBrailleBlock) { // 처음 2m 이내로 들어올 때
                isUserNearBrailleBlock = true; // 상태 업데이트
            }
            Toast.makeText(context, "점자블록 근처입니다.", Toast.LENGTH_SHORT).show(); // 계속 신호 발생
        } else {
            if (isUserNearBrailleBlock) { // 처음 2m 이상으로 벗어났을 때
                isUserNearBrailleBlock = false; // 상태 업데이트
            }
            // 2m 이상일 때도 신호를 계속 발생
            Toast.makeText(context, "점자블록에서 2m 이상 떨어졌습니다.", Toast.LENGTH_SHORT).show(); // 계속 신호 발생
        }
    }
}
