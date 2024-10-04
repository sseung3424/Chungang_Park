package com.chungangpark.parknavigator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PolylineOverlay;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PathFinder {

    private Context context;
    private NaverMap naverMap;
    private String apiKeyId;
    private String apiKeySecret;

    public PathFinder(Context context, NaverMap naverMap, String apiKeyId, String apiKeySecret) {
        this.context = context;
        this.naverMap = naverMap;
        this.apiKeyId = apiKeyId;
        this.apiKeySecret = apiKeySecret;
    }

    // 경로 탐색 메서드: 출발지와 목적지 경로를 찾고 지도에 표시
    public void findPath(LatLng startLatLng, LatLng endLatLng) {
        // Directions API URL 생성
        @SuppressLint("DefaultLocale") String directionsApiUrl = String.format(
                "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=%f,%f&goal=%f,%f&option=trafast",
                startLatLng.longitude, startLatLng.latitude,
                endLatLng.longitude, endLatLng.latitude
        );

        // OkHttp 클라이언트를 사용하여 API 요청
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(directionsApiUrl)
                .addHeader("X-NCP-APIGW-API-KEY-ID", apiKeyId)
                .addHeader("X-NCP-APIGW-API-KEY", apiKeySecret)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 실패 시 UI 스레드에서 Toast 메시지 표시
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "경로 요청 실패", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // 응답 데이터를 파싱
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray routes = jsonObject.getJSONObject("route").getJSONArray("traoptimal");

                        // 경로 좌표를 담을 리스트
                        List<LatLng> routePoints = new ArrayList<>();

                        // 경로 정보에서 경로 좌표 파싱
                        JSONArray path = routes.getJSONObject(0).getJSONArray("path");
                        for (int i = 0; i < path.length(); i++) {
                            JSONArray point = path.getJSONArray(i);
                            routePoints.add(new LatLng(point.getDouble(1), point.getDouble(0)));
                        }

                        // UI 스레드에서 경로 표시
                        new Handler(Looper.getMainLooper()).post(() -> {
                            // PolylineOverlay에 경로 설정
                            PolylineOverlay polyline = new PolylineOverlay();
                            polyline.setCoords(routePoints);
                            polyline.setMap(naverMap); // 경로를 지도에 표시

                            // 출발지와 도착지에 마커 추가
                            Marker startMarker = new Marker();
                            startMarker.setPosition(startLatLng);
                            startMarker.setCaptionText("출발지");
                            startMarker.setMap(naverMap);

                            Marker endMarker = new Marker();
                            endMarker.setPosition(endLatLng);
                            endMarker.setCaptionText("도착지");
                            endMarker.setMap(naverMap);

                            Toast.makeText(context, "경로를 찾았습니다!", Toast.LENGTH_SHORT).show();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "경로 요청 실패: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
