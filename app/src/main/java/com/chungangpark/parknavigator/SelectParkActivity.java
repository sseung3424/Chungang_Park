package com.chungangpark.parknavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;

public class SelectParkActivity extends AppCompatActivity {
    private NaverMap naverMap;

    // 한강 공원의 위치를 정의합니다.
    private static final LatLng YEUIDO_PARK = new LatLng(37.5283169, 126.9328034); // 여의도 한강 공원 좌표
    private static final LatLng MANGWON_PARK = new LatLng(37.5580, 126.9027);
    private static final LatLng JAMSIL_PARK = new LatLng(37.5100, 127.1000); // 잠실 한강 공원 좌표

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_park);

        // 여의도 버튼 (LinearLayout으로 변경)
        LinearLayout btnYeouido = findViewById(R.id.btn_yeouido);
        btnYeouido.setOnClickListener(v -> {
            // MainActivity로 이동하고 선택한 공원 정보 전달
            Intent intent = new Intent(SelectParkActivity.this, MainActivity.class);
            intent.putExtra("park_name", "여의도");
            startActivity(intent);
        });

        // 잠실 버튼 (LinearLayout으로 변경)
        LinearLayout btnJamsil = findViewById(R.id.btn_jamsil);
        btnJamsil.setOnClickListener(v -> {
            // MainActivity로 이동하고 선택한 공원 정보 전달
            Intent intent = new Intent(SelectParkActivity.this, MainActivity.class);
            intent.putExtra("park_name", "잠실");
            startActivity(intent);
        });

        // 망원 버튼 (LinearLayout으로 변경)
        LinearLayout btnMangwon = findViewById(R.id.btn_mangwon);
        btnMangwon.setOnClickListener(v -> {
            // MainActivity로 이동하고 선택한 공원 정보 전달
            Intent intent = new Intent(SelectParkActivity.this, MainActivity.class);
            intent.putExtra("park_name", "망원");
            startActivity(intent);
        });
    }
    // 한강 공원 목록 다이얼로그 표시
    public void showParkListDialog() {
        final String[] parkList = {"여의도 한강 공원", "망원 한강 공원","잠실 한강 공원"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("한강 공원 선택")
                .setItems(parkList, (dialog, which) -> {
                    switch (which) {
                        case 0: // 여의도 한강 공원 선택
                            moveToPark(YEUIDO_PARK);
                            break;
                        case 1: // 망원 한강 공원 선택
                            moveToPark(MANGWON_PARK);
                            break;
                        case 2: // 망원 한강 공원 선택
                            moveToPark(JAMSIL_PARK);
                            break;
                    }
                })

                .show();
    }

    // 공원의 이름에 따라 올바른 LatLng 좌표를 전달하는 메서드
    public void moveToSelectedPark(String parkName) {
        LatLng parkLocation = null;

        switch (parkName) {
            case "여의도":
                parkLocation = YEUIDO_PARK;
                break;
            case "망원":
                parkLocation = MANGWON_PARK;
                break;
            case "잠실":
                parkLocation = JAMSIL_PARK;
                break;
            default:
                Toast.makeText(this, "알 수 없는 공원 선택", Toast.LENGTH_SHORT).show();
                return;
        }

        if (parkLocation != null) {
            moveToPark(parkLocation);  // LatLng 좌표를 사용하여 공원으로 이동
        }
    }

    // 선택한 공원으로 지도 이동
    private void moveToPark(LatLng parkLocation) {

        // 줌 레벨을 12로 설정하여 더 넓게 보기
        CameraUpdate cameraUpdate = CameraUpdate.zoomTo(15);
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
}
