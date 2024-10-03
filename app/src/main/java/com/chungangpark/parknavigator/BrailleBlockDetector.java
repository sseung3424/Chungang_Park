package com.chungangpark.parknavigator;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.PolylineOverlay;

import java.util.ArrayList;
import java.util.List;

public class BrailleBlockDetector {
    NavigationManager nm = new NavigationManager();
    // 클래스에 점자블록 리스트 추가
    private List<PolylineOverlay> brailleBlocks = new ArrayList<>();
    // 점자블록 경계를 벗어났는지 확인하는 함수 추가
    public boolean isUserOutsideBrailleBlocks(LatLng userPosition) {
        for (PolylineOverlay block : brailleBlocks) {
            if (nm.getDistanceFromPolyline(block, userPosition) <= block.getWidth() / 1000.0) {
                return false; //

                // 점자블록 내에 있으면 false 반환
            }
        }
        return true; // 모든 점자블록 경계 밖에 있으면 true 반환
    }

    // 점형 점자블록 위에 사용자가 있는지 확인하는 함수 추가
    public boolean isUserOnDotBrailleBlock(LatLng userPosition) {
        for (PolylineOverlay dotBlock : brailleBlocks) {
            // 점형 점자블록인 경우에만 체크
            if (dotBlock.getColor() == 0xFFFF00A5) { // 핑크색으로 식별
                if (nm.getDistanceFromPolyline(dotBlock, userPosition) <= dotBlock.getWidth() / 1000.0) {
                    return true; // 사용자가 점형 점자블록 위에 있음
                }
            }
        }
        return false; // 사용자가 점형 점자블록 위에 없음
    }

    // 특정 좌표를 기준으로 가장 가까운 점자블록 좌표를 반환하는 함수
    public LatLng getNearestBrailleBlock(LatLng position) {
        LatLng nearestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (PolylineOverlay block : brailleBlocks) {
            List<LatLng> coords = block.getCoords();
            for (LatLng coord : coords) {
                double distance = nm.distance(position, coord);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPoint = coord;
                }
            }
        }
        return nearestPoint;
    }
    // 두 지점 간의 거리 계산 함수 (LatLng 좌표 사용)
    public double distance(LatLng p1, LatLng p2) {
        double latDiff = Math.toRadians(p1.latitude - p2.latitude);
        double lngDiff = Math.toRadians(p1.longitude - p2.longitude);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(p1.latitude)) * Math.cos(Math.toRadians(p2.latitude)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double earthRadius = 6371000; // 지구 반지름 (미터 단위)
        return earthRadius * c;
    }
}
