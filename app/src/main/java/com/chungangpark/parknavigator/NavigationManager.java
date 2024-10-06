package com.chungangpark.parknavigator;

import static com.chungangpark.parknavigator.MainActivity.brailleBlocks;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.PolylineOverlay;

import java.util.ArrayList;
import java.util.List;

public class NavigationManager {
    // 사용자 위치와 폴리라인 사이의 거리 계산 함수 추가
    public double getDistanceFromPolyline(PolylineOverlay polyline, LatLng userPosition) {
        List<LatLng> coords = polyline.getCoords();
        double minDistance = Double.MAX_VALUE;

        // 각 선분에 대해 최소 거리 계산
        for (int i = 0; i < coords.size() - 1; i++) {
            LatLng start = coords.get(i);
            LatLng end = coords.get(i + 1);
            double distance = distanceToSegment(userPosition, start, end);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    // 점과 선분 사이의 거리를 계산하는 헬퍼 함수 추가
    private double distanceToSegment(LatLng p, LatLng v, LatLng w) {
        double l2 = distance(v, w);
        if (l2 == 0.0) return distance(p, v);
        double t = ((p.longitude - v.longitude) * (w.longitude - v.longitude) + (p.latitude - v.latitude) * (w.latitude - v.latitude)) / l2;
        t = Math.max(0, Math.min(1, t));
        LatLng projection = new LatLng(v.latitude + t * (w.latitude - v.latitude), v.longitude + t * (w.longitude - v.longitude));
        return distance(p, projection);
    }
    // 점과 선분 사이의 최소 거리 계산 함수
    public double distanceToLineSegment(LatLng point, LatLng lineStart, LatLng lineEnd) {
        double x0 = point.latitude;
        double y0 = point.longitude;
        double x1 = lineStart.latitude;
        double y1 = lineStart.longitude;
        double x2 = lineEnd.latitude;
        double y2 = lineEnd.longitude;

        double A = x0 - x1;
        double B = y0 - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = len_sq != 0 ? dot / len_sq : -1;

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = x0 - xx;
        double dy = y0 - yy;
        return Math.sqrt(dx * dx + dy * dy) * 100000;  // 미터 단위로 변환
    }
    // 두 점 사이의 거리 계산 헬퍼 함수 추가
    public double distance(LatLng p1, LatLng p2) {
        double dx = p1.longitude - p2.longitude;
        double dy = p1.latitude - p2.latitude;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // 시작점에서 화장실 위치까지 경로를 찾는 함수 (단순히 가장 가까운 점자블록들로 이어지는 경로를 계산)
    private List<LatLng> findPathToToilet(LatLng start, LatLng end) {
        List<LatLng> path = new ArrayList<>();
        LatLng current = start;

        while (distance(current, end) > 0.0001) { // 임의의 오차 범위 설정
            current = getNearestBrailleBlock(current);
            path.add(current);
            if (current.equals(end)) {
                break;
            }
        }
        path.add(end);
        return path;
    }
    // 특정 좌표를 기준으로 가장 가까운 점자블록 좌표를 반환하는 함수
    private LatLng getNearestBrailleBlock(LatLng position) {
        LatLng nearestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (PolylineOverlay block : brailleBlocks) {
            List<LatLng> coords = block.getCoords();
            for (LatLng coord : coords) {
                double distance = distance(position, coord);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPoint = coord;
                }
            }
        }
        return nearestPoint;
    }
}
