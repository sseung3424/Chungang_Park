package com.chungangpark.parknavigator;

// 점형 점자블록 추가
// 선형 점자 블록 추가
import com.naver.maps.map.overlay.PolylineOverlay;
// latlng 클래스 임포트
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.Marker;
import android.os.Build;

// 블루투스 관련 라이브러리 임포트 확인
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.location.Location;
import android.widget.LinearLayout;
import android.widget.Toast; // Toast 추가
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList; // ArrayList 추가
import java.util.List; // List 추가
import java.util.UUID; // UUID 추가

        import android.widget.Toast; // Toast 추가

        import java.util.ArrayList; // ArrayList 추가
import java.util.List; // List 추가

        import android.animation.Animator;

        import android.os.Bundle;
import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;

        import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
        import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

        import com.naver.maps.map.CameraUpdate;
        import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;

        import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 1;  // 권한 요청 코드

    // 한강 공원의 위치를 정의합니다.
    private static final LatLng YEUIDO_PARK = new LatLng(37.5283169, 126.9328034); // 여의도 한강 공원 좌표
    private static final LatLng MANGWON_PARK = new LatLng(37.5580, 126.9027);
    private static final LatLng JAMSIL_PARK = new LatLng(37.5100, 127.1000); // 잠실 한강 공원 좌표

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private PathFinder pathFinder;
    MarkerManager markerManager = new MarkerManager(naverMap);
    private boolean isNavigating = false;  // 길찾기 상태를 추적하는 플래그
    private LinearLayout cancelNavigationButton;
    private Animator animator;
    private ObstacleManager ObstacleManager;
    private BrailleBlockManager brailleBlockManager;
    private SectionManager sectionManager;
    private static final UUID HC06_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // HC-06 UUID
    private static final String HC06_DEVICE_NAME = "HC-06"; // HC-06 장치 이름

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendSignalButton = findViewById(R.id.send_signal_button);
        // BluetoothAdapter 초기화
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // BluetoothAdapter가 null인지 확인 (블루투스를 지원하는지 확인)
        if (bluetoothAdapter == null) {
            // 이 기기는 블루투스를 지원하지 않습니다.
            Toast.makeText(this, "This device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
            finish(); // 앱 종료 또는 다른 처리
            return;
        }
        // 블루투스 권한 확인 및 요청
        checkBluetoothPermissions();
        // 이미 페어링 및 연결된 장치로 소켓 생성
        connectToPairedHC06();


        // 버튼 클릭 시 신호 전송
        sendSignalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToArduino('7');  // 7이라는 신호 전송
            }
        });


// SectionManager 인스턴스 생성
        // 한강 공원 목록 버튼 설정
        LinearLayout selectParkButton = findViewById(R.id.btn_select_park);
        selectParkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParkListDialog();
            }
        });

        // '주변 정보 안내' 버튼 설정
        LinearLayout nearbyInfoButton = findViewById(R.id.btn_nearby_info);
        nearbyInfoButton.setOnClickListener(v -> showNearbyInfo());

        // 취소 버튼 찾기
        cancelNavigationButton = findViewById(R.id.btn_cancel_navigation);

        // 취소 버튼 클릭 이벤트
        cancelNavigationButton.setOnClickListener(v -> {
            cancelNavigation();  // 길찾기 취소
        });

        // 길찾기 버튼 설정
        LinearLayout findPathButton = findViewById(R.id.btn_find_path);
        findPathButton.setEnabled(false);  // 초기에는 비활성화 상태

        findPathButton.setOnClickListener(v -> {
                    if (naverMap == null || markerManager == null) {
                        Toast.makeText(MainActivity.this, "길찾기 기능이 아직 초기화되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isNavigating) {
                        // 길찾기 중이면 취소 처리
                        cancelNavigation();
                    } else {
                        // 길찾기 대화창 표시
                        showFindPathDialog();
                    }
                });

        // 길찾기 버튼을 눌렀을 때 동작 정의
        findPathButton.setOnClickListener(v -> {
            if (naverMap == null || markerManager == null) {
                Toast.makeText(MainActivity.this, "길찾기 기능이 아직 초기화되지 않았습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 길찾기 대화창 표시
            showFindPathDialog();
        });

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

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // 네이버 지도 객체 설정
        this.naverMap = naverMap;

        // 지도 UI 설정 (줌 버튼)
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(false);

        // 마커 매니저 생성 및 마커 추가
        markerManager.addMarkers();

        // PathFinder 객체 초기화
        pathFinder = new PathFinder(this, naverMap);  // 여기에 PathFinder 초기화

        // 위치 오버레이 설정
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setSubIcon(LocationOverlay.DEFAULT_SUB_ICON_ARROW);
        locationOverlay.setCircleOutlineWidth(0); // 원 테두리 제거

        // 위치 추적 설정
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        // findPathButton 활성화
        LinearLayout findPathButton = findViewById(R.id.btn_find_path);
        findPathButton.setEnabled(true); // 지도와 PathFinder가 준비되면 버튼 활성화

        // 선택한 공원으로 지도 이동
        String parkName = getIntent().getStringExtra("park_name");
        if (parkName != null) {
            moveToSelectedPark(parkName);
        }

        // 점자 블록 매니저 생성 및 점자 블록 추가
        ObstacleManager = new ObstacleManager(this);
        ObstacleManager.addBrailleBlockonMap(naverMap);  // 점자 블록을 지도에 추가
        // BrailleBlockManager 초기화 및 점자블록 추가
        brailleBlockManager = new BrailleBlockManager(this, outputStream);
        brailleBlockManager.addBrailleBlockOnMap(naverMap);  // 지도 준비 완료 후 점자블록 추가

        sectionManager = new SectionManager(this);
        sectionManager.addSectiononMap(naverMap);
    }
    // 신호를 아두이노로 전송하는 메서드
    private void sendDataToArduino(int data) {
        if (outputStream != null) {
            try {
                outputStream.write("7".getBytes()); // input 7을 전송하는 예시
                Toast.makeText(this, "데이터 전송 완료", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "데이터 전송 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void connectToPairedHC06() {
        try {

            BluetoothDevice hc06Device = bluetoothAdapter.getRemoteDevice("98:DA:60:0B:B8:F9");  // 예시 MAC 주소
            bluetoothSocket = hc06Device.createRfcommSocketToServiceRecord(HC06_UUID);

            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            Toast.makeText(this, "HC-06에 성공적으로 연결되었습니다.", Toast.LENGTH_SHORT).show();

        } catch (SecurityException e) {
            // 권한 문제가 발생했을 때 처리
            Toast.makeText(this, "블루투스 권한 오류: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            checkBluetoothPermissions();  // 다시 권한 요청
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "HC-06 연결 실패", Toast.LENGTH_SHORT).show();
        }
    }
    // 블루투스 권한 체크 및 요청
    private void checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // 안드로이드 12 이상에서는 추가 권한 필요
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN},
                        PERMISSION_REQUEST_CODE);
            }
        } else {
            // 안드로이드 12 미만에서는 BLUETOOTH와 BLUETOOTH_ADMIN 권한만 필요
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }
    
    // 길찾기 선택 다이얼로그 표시
    private void showFindPathDialog() {
        final String[] options = {"화장실", "안내소", "매점"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("길찾기 옵션 선택")
                .setItems(options, (dialog, which) -> {
                    LatLng userLocation = getUserCurrentLocation();  // 사용자 위치 가져오기
                    if (userLocation == null) {
                        Toast.makeText(MainActivity.this, "현재 위치를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    LatLng destination = null;

                    switch (which) {
                        case 0: // 화장실 선택
                            destination = findNearestLocation(userLocation, markerManager.getToiletMarkers());
                            break;
                        case 1: // 안내소 선택
                            destination = findNearestLocation(userLocation, markerManager.getInformationMarkers());
                            break;
                        case 2: // 매점 선택
                            destination = findNearestLocation(userLocation, markerManager.getStoreMarkers());
                            break;
                    }

                    if (destination != null) {
                        moveToDestination(destination);
                    } else {
                        Toast.makeText(MainActivity.this, "목적지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    // 클래스에 점자블록 리스트 추가
    public static List<PolylineOverlay> brailleBlocks = new ArrayList<>();

    // 주변 정보 안내 버튼을 눌렀을 때 호출되는 메서드
    private void showNearbyInfo() {
        LatLng userLocation = getUserCurrentLocation(); // 사용자의 현재 위치 가져오기
        if (userLocation != null && markerManager != null) {
            List<String> nearbyLocations = markerManager.getNearbyMarkers(userLocation, 30); // 30m 반경 내 마커들 가져오기
            if (nearbyLocations.isEmpty()) {
                Toast.makeText(this, "주변 30m 내에 안내할 장소가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                for (String location : nearbyLocations) {
                    Toast.makeText(this, location + "가(이) 30m 내에 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "현재 위치를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 가장 가까운 위치 찾기
    private LatLng findNearestLocation(LatLng userLocation, List<Marker> markers) {
        LatLng closestLocation = null;
        double minDistance = Double.MAX_VALUE;

        for (Marker marker : markers) {
            double distance = getDistance(userLocation, marker.getPosition());
            if (distance < minDistance) {
                minDistance = distance;
                closestLocation = marker.getPosition();
            }
        }
        return closestLocation;
    }

    // 두 지점 간의 거리 계산 (미터 단위)
    private double getDistance(LatLng point1, LatLng point2) {
        float[] results = new float[1];
        Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, results);
        return results[0]; // 거리 반환 (미터 단위)
    }

    private void moveToDestination(LatLng destination) {
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(destination).zoomTo(17);
        naverMap.moveCamera(cameraUpdate);

        // 사용자 위치 가져오기
        LatLng userLocation = getUserCurrentLocation();
        if (userLocation == null) {
            Toast.makeText(this, "현재 위치를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 점자블록 경로 가져오기
        PolylineOverlay braillePolyline = getBraillePolyline();
        if (braillePolyline == null) {
            Toast.makeText(this, "점자 블록 경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 목적지 설정 및 경로 안내 시작 (PathFinder의 startNavigation 호출)
        pathFinder.startNavigation(userLocation, braillePolyline, destination);
        Toast.makeText(this, "목적지로 안내를 시작합니다.", Toast.LENGTH_SHORT).show();

        // 길찾기 중 상태로 설정
        isNavigating = true;
        Toast.makeText(this, "목적지로 안내를 시작합니다.", Toast.LENGTH_SHORT).show();

        // 취소 버튼 보이기
        cancelNavigationButton.setVisibility(View.VISIBLE);
    }

    // 길찾기 취소
    private void cancelNavigation() {
        pathFinder.shutdownTTS();  // TTS 종료

        isNavigating = false;  // 길찾기 상태 초기화
        Toast.makeText(this, "길찾기가 취소되었습니다.", Toast.LENGTH_SHORT).show();

        // 취소 버튼 숨기기
        cancelNavigationButton.setVisibility(View.GONE);
    }

    private LatLng getUserCurrentLocation() {
        if (naverMap == null) {
            Toast.makeText(this, "지도가 아직 준비되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return null;
        }

        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        return new LatLng(locationOverlay.getPosition().latitude, locationOverlay.getPosition().longitude);
    }

    private PolylineOverlay getBraillePolyline() {
        if (brailleBlocks.isEmpty()) {
            Toast.makeText(this, "점자 블록 경로가 없습니다.", Toast.LENGTH_SHORT).show();
            return null;
        }
        return brailleBlocks.get(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TTS 종료
        if (pathFinder != null) {
            pathFinder.shutdownTTS();
        }
    }

    // 한강 공원 목록 다이얼로그 표시
    private void showParkListDialog() {
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
    private void moveToSelectedPark(String parkName) {
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
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(parkLocation).zoomTo(15);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                Toast.makeText(this, "블루투스 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "필수 권한을 승인해야 블루투스를 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
            }
            return;
        }
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