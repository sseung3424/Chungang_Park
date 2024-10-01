package com.chungangpark.parknavigator;

// 점형 점자블록 추가
import com.naver.maps.map.overlay.Marker;
// 선형 점자 블록 추가
import com.naver.maps.map.overlay.PolylineOverlay;
// latlng 클래스 임포트
import com.naver.maps.geometry.LatLng;

// 블루투스 관련 라이브러리 임포트 확인
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast; // Toast 추가
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList; // ArrayList 추가
import java.util.List; // List 추가
import java.util.UUID; // UUID 추가

import java.util.Arrays;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.UiSettings;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private NaverMap naverMap;

    // 필드 추가 (클래스 맨 위에 추가)
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;

    // 블루투스 설정 함수 추가
    /*private void setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_SHORT).show(); // this 대신 getApplicationContext() 사용
            return;
        }

        // 블루투스 장치 연결 (아두이노의 블루투스 모듈 MAC 주소와 UUID 설정)
        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice("00:00:00:00:00:00"); // 아두이노 블루투스 모듈의 MAC 주소로 변경
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // 일반적인 시리얼 통신 UUID
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Bluetooth connection failed", Toast.LENGTH_SHORT).show();
        }
    }*/

    // 아두이노로 진동 신호 전송 함수 추가
    private void sendVibrationSignal() {
        try {
            if (outputStream != null) {
                outputStream.write("V".getBytes()); // 진동 신호 전송
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 진동 신호 전송 함수 추가
        try {
            if (outputStream != null) {
                outputStream.write("V".getBytes()); // 진동 신호 전송
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MapFragment 가져오기
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(new NaverMapOptions());
            getSupportFragmentManager().beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        // 지도 초기화
        mapFragment.getMapAsync(this);

        /*// 블루투스 설정 함수 호출
        setupBluetooth();*/
    }

    // 클래스에 점자블록 리스트 추가
    private List<PolylineOverlay> brailleBlocks = new ArrayList<>();

    // 사용자 위치와 폴리라인 사이의 거리 계산 함수 추가
    private double getDistanceFromPolyline(PolylineOverlay polyline, LatLng userPosition) {
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

    // 두 점 사이의 거리 계산 헬퍼 함수 추가
    private double distance(LatLng p1, LatLng p2) {
        double dx = p1.longitude - p2.longitude;
        double dy = p1.latitude - p2.latitude;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // 점자블록 경계를 벗어났는지 확인하는 함수 추가
    private boolean isUserOutsideBrailleBlocks(LatLng userPosition) {
        for (PolylineOverlay block : brailleBlocks) {
            if (getDistanceFromPolyline(block, userPosition) <= block.getWidth() / 1000.0) {
                return false; // 점자블록 내에 있으면 false 반환
            }
        }
        return true; // 모든 점자블록 경계 밖에 있으면 true 반환
    }


    // 진동 신호 반복 전송 함수 추가
    private void sendRepeatedVibrationSignal(int times) {
        new Thread(() -> {
            for (int i = 0; i < times; i++) {
                sendVibrationSignal(); // 진동 신호 전송
                try {
                    Thread.sleep(500); // 0.5초 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 점형 점자블록 위에 사용자가 있는지 확인하는 함수 추가
    private boolean isUserOnDotBrailleBlock(LatLng userPosition) {
        for (PolylineOverlay dotBlock : brailleBlocks) {
            // 점형 점자블록인 경우에만 체크
            if (dotBlock.getColor() == 0xFFFF00A5) { // 핑크색으로 식별
                if (getDistanceFromPolyline(dotBlock, userPosition) <= dotBlock.getWidth() / 1000.0) {
                    return true; // 사용자가 점형 점자블록 위에 있음
                }
            }
        }
        return false; // 사용자가 점형 점자블록 위에 없음
    }
    // 아두이노로 진동 신호 전송 함수 (진동 센서 번호에 맞게)
    private void sendVibrationSignal(int sensorNumber) {
        try {
            if (outputStream != null) {
                // 진동 센서 번호에 따라 신호 전송
                if (sensorNumber == 1) {
                    outputStream.write("1".getBytes());  // 진동 센서 1 작동 신호 전송
                } else if (sensorNumber == 2) {
                    outputStream.write("2".getBytes());  // 진동 센서 2 작동 신호 전송
                } else if (sensorNumber == 3) {
                    outputStream.write("3".getBytes());  // 진동 센서 3 작동 신호 전송
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 장애물 앞 진동 트리거 (진동 센서 번호에 따라)
    private void triggerVibrationAtIntersection(LatLng userPosition) {
        // 좌표와 진동 센서 번호를 매핑
        List<VibrationTrigger> triggers = Arrays.asList(
                new VibrationTrigger(new LatLng(37.52746540, 126.93278579), 2, 3),
                new VibrationTrigger(new LatLng(37.52749844, 126.93282825), 1, 2),
                new VibrationTrigger(new LatLng(37.52746078, 126.93283010), 1, 3),
                new VibrationTrigger(new LatLng(37.52719621, 126.93243238), 1, 3),
                new VibrationTrigger(new LatLng(37.52713827, 126.93239991), 2, 3),
                new VibrationTrigger(new LatLng(37.52677942, 126.93364775), 2, 3),
                new VibrationTrigger(new LatLng(37.52674941, 126.93364060), 1, 3),
                new VibrationTrigger(new LatLng(37.52673475, 126.93369419), 1, 2),
                new VibrationTrigger(new LatLng(37.52674941, 126.93364060), 1, 3),
                new VibrationTrigger(new LatLng(37.52656957, 126.93325711), 2, 3),
                new VibrationTrigger(new LatLng(37.52657473, 126.93330811), 1, 3),
                new VibrationTrigger(new LatLng(37.52659294, 126.93330747), 1, 2),
                new VibrationTrigger(new LatLng(37.52656118, 126.93323845), 1, 3),
                new VibrationTrigger(new LatLng(37.52655245, 126.93318723), 1, 2),
                new VibrationTrigger(new LatLng(37.52653469, 126.93323310), 2, 3),
                new VibrationTrigger(new LatLng(37.52645537, 126.93398751), 1, 2, 3),
                new VibrationTrigger(new LatLng(37.52644432, 126.93393378), 1, 2, 3),
                new VibrationTrigger(new LatLng(37.52640692, 126.93398781), 1, 2, 3),
                new VibrationTrigger(new LatLng(37.52623683, 126.93361226), 1, 2),
                new VibrationTrigger(new LatLng(37.52623836, 126.93364102), 1, 3),
                new VibrationTrigger(new LatLng(37.52621122, 126.93364469), 2, 3),
                new VibrationTrigger(new LatLng(37.52607395, 126.93426738), 2, 3),
                new VibrationTrigger(new LatLng(37.52604918, 126.93425169), 1, 3),
                new VibrationTrigger(new LatLng(37.52601184, 126.93394634), 1, 3),
                new VibrationTrigger(new LatLng(37.52601374, 126.93390651), 1, 2)
        );

        // 좌표가 설정된 위치에 도달했는지 확인하여 진동 작동
        for (VibrationTrigger trigger : triggers) {
            if (distance(userPosition, trigger.getPosition()) <= 0.005) {
                sendVibrationSignal(trigger.getSensor1());
                sendVibrationSignal(trigger.getSensor2());
            }
        }
    }
    // VibrationTrigger 클래스 추가 (좌표와 진동 센서를 매핑하기 위한 헬퍼 클래스)
    class VibrationTrigger {
        private LatLng position;
        private int sensor1;
        private int sensor2;
        private int sensor3;

        public VibrationTrigger(LatLng position, int sensor1, int sensor2) {
            this.position = position;
            this.sensor1 = sensor1;
            this.sensor2 = sensor2;
        }

        public VibrationTrigger(LatLng position, int sensor1, int sensor2, int sensor3) {
            this.position = position;
            this.sensor1 = sensor1;
            this.sensor2 = sensor2;
            this.sensor3 = sensor3;
        }

        public LatLng getPosition() {
            return position;
        }

        public int getSensor1() {
            return sensor1;
        }

        public int getSensor2() {
            return sensor2;
        }

        public int getSensor3() {
            return sensor3;
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // 네이버 지도 객체 가져오기
        this.naverMap = naverMap;

        // 지도 UI 설정 (줌 버튼 등)
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(true); // 줌 컨트롤 설정

        // 위치 추적 모드를 None으로 설정 (원하는 경우 Follow로 변경 가능)
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);

        // 선형 점자블록 추가
        addLinearBrailleBlock(naverMap, new LatLng( 37.52709831, 126.93245824 ), new LatLng(37.52713827, 126.93239991));
        addLinearBrailleBlock(naverMap, new LatLng(37.52719621, 126.93243238), new LatLng(37.52746540, 126.93278579));
        addLinearBrailleBlock(naverMap, new LatLng(37.52746078, 126.93283010), new LatLng(37.52677942, 126.93364775));
        addLinearBrailleBlock(naverMap, new LatLng(37.52674941, 126.93364060), new LatLng(37.52672805, 126.93358961));
        addLinearBrailleBlock(naverMap, new LatLng(37.52672805, 126.93358961), new LatLng(37.52659294, 126.93330747));
        addLinearBrailleBlock(naverMap, new LatLng(37.52656957, 126.93325711), new LatLng(37.52656118, 126.93323845));
        addLinearBrailleBlock(naverMap, new LatLng(37.52657473, 126.93330811), new LatLng(37.52656231, 126.93341181));
        addLinearBrailleBlock(naverMap, new LatLng(37.52656231, 126.93341181), new LatLng(37.52655814, 126.93351839));
        addLinearBrailleBlock(naverMap, new LatLng(37.52655814, 126.93351839), new LatLng(37.52654010, 126.93361643));
        addLinearBrailleBlock(naverMap, new LatLng(37.52654010, 126.93361643), new LatLng(37.52650548, 126.93375344));
        addLinearBrailleBlock(naverMap, new LatLng(37.52650548, 126.93375344), new LatLng(37.52644432, 126.93393378));

        addLinearBrailleBlock(naverMap, new LatLng(37.52749844, 126.93282825), new LatLng(37.52753334, 126.93287541));
        addLinearBrailleBlock(naverMap, new LatLng(37.52673475, 126.93369419), new LatLng(37.52646021, 126.93394120));
        addLinearBrailleBlock(naverMap, new LatLng(37.52640692, 126.93398781), new LatLng(37.52607395, 126.93426738));
        addLinearBrailleBlock(naverMap, new LatLng(37.52650548, 126.93375344), new LatLng(37.52644432, 126.93393378));
        addLinearBrailleBlock(naverMap, new LatLng( 37.52645537, 126.93398751), new LatLng(37.52678618, 126.93435865));

        addLinearBrailleBlock(naverMap, new LatLng(37.52672839, 126.93294539), new LatLng(37.52655245, 126.93318723));

        addLinearBrailleBlock(naverMap, new LatLng(37.52653469, 126.93323310), new LatLng(37.52623683, 126.93361226));
        addLinearBrailleBlock(naverMap, new LatLng( 37.52621122, 126.93364469), new LatLng(37.52601374, 126.93390651));

        addLinearBrailleBlock(naverMap, new LatLng(37.52601184, 126.93394634), new LatLng(37.52605962, 126.93401307));
        addLinearBrailleBlock(naverMap, new LatLng(37.52606486, 126.93406153 ), new LatLng( 37.52605962, 126.93401307));
        addLinearBrailleBlock(naverMap, new LatLng(  37.52604918, 126.93425169  ), new LatLng(37.52606486, 126.93406153));

        // 점형 점자블록 추가
        addDotBrailleBlock(naverMap, new LatLng(37.52746540, 126.93278579), new LatLng(37.52748075, 126.93280613));
        addDotBrailleBlock(naverMap, new LatLng(37.52746078, 126.93283010), new LatLng(37.52748075, 126.93280613));
        addDotBrailleBlock(naverMap, new LatLng(37.52749844, 126.93282825), new LatLng(37.52748075, 126.93280613));
        // 선형 점자블록에서 점형 점자블록으로 바뀌는 구간에서만 작동하도록
        // 37.52746540, 126.93278579 2번 3번
        // 37.52749844, 126.93282825 1번 2번
        // 37.52746078, 126.93283010 1번 3번
        // 37.52719621, 126.93243238 1번 3번
        // 37.52713827, 126.93239991 2번 3번
        // 37.52677942, 126.93364775 2번 3번
        // 37.52674941, 126.93364060 1번 3번
        // 37.52673475, 126.93369419 1번 2번
        // 37.52674941, 126.93364060 1번 3번
        // 37.52656957, 126.93325711 2번 3번
        // 37.52657473, 126.93330811 1번 3번
        // 37.52659294, 126.93330747 1번 2번
        // 37.52656118, 126.93323845 1번 3번
        // 37.52655245, 126.93318723 1번 2번
        // 37.52653469, 126.93323310 2번 3번
        // 37.52645537, 126.93398751 1번 2번 3번
        // 37.52644432, 126.93393378 1번 2번 3번
        // 37.52640692, 126.93398781 1번 2번 3번
        // 37.52645537, 126.93398751 1번 2번 3번
        // 37.52623683, 126.93361226 1번 2번
        // 37.52623836, 126.93364102 1번 3번
        // 37.52621122, 126.93364469 2번 3번
        //37.52607395, 126.93426738 2번 3번
        // 37.52604918, 126.93425169 1번 3번
        // 37.52601184, 126.93394634 1번 3번
        // 37.52601374, 126.93390651 1번 2번
        addDotBrailleBlock(naverMap, new LatLng(37.5666102, 126.9783881), new LatLng(37.5668202, 126.9786881));
        addDotBrailleBlock(naverMap, new LatLng(37.52754974, 126.93289687), new LatLng(37.52753334, 126.93287541));

        addDotBrailleBlock(naverMap, new LatLng(37.52677942, 126.93364775), new LatLng(37.52676205, 126.93366983));
        addDotBrailleBlock(naverMap, new LatLng(37.52674941, 126.93364060), new LatLng(37.52676205, 126.93366983));
        addDotBrailleBlock(naverMap, new LatLng(37.52673475, 126.93369419), new LatLng(37.52676205, 126.93366983));

        addDotBrailleBlock(naverMap, new LatLng(37.52656957, 126.93325711), new LatLng( 37.52657932, 126.93327844));
        addDotBrailleBlock(naverMap, new LatLng(37.52659294, 126.93330747), new LatLng( 37.52657932, 126.93327844));
        addDotBrailleBlock(naverMap, new LatLng(37.52657473, 126.93330811), new LatLng( 37.52657932, 126.93327844));


        addDotBrailleBlock(naverMap, new LatLng(37.52656118, 126.93323845), new LatLng( 37.52654361, 126.93320511));
        addDotBrailleBlock(naverMap, new LatLng(37.52655245, 126.93318723), new LatLng( 37.52654361, 126.93320511));
        addDotBrailleBlock(naverMap, new LatLng(37.52653469, 126.93323310), new LatLng( 37.52654361, 126.93320511));

        addDotBrailleBlock(naverMap, new LatLng(37.52719621, 126.93243238), new LatLng( 37.52715291, 126.93237505));
        addDotBrailleBlock(naverMap, new LatLng(37.52713827, 126.93239991), new LatLng( 37.52715291, 126.93237505));

        addDotBrailleBlock(naverMap, new LatLng(37.52646021, 126.93394120), new LatLng( 37.52643553, 126.93396365));
        addDotBrailleBlock(naverMap, new LatLng(37.52644432, 126.93393378), new LatLng( 37.52643553, 126.93396365));
        addDotBrailleBlock(naverMap, new LatLng(37.52640692, 126.93398781), new LatLng( 37.52643553, 126.93396365));
        addDotBrailleBlock(naverMap, new LatLng(37.52645537, 126.93398751), new LatLng( 37.52643553, 126.93396365));

        addDotBrailleBlock(naverMap, new LatLng(37.52678618, 126.93435865), new LatLng( 37.52680387, 126.93437803));



        addDotBrailleBlock(naverMap, new LatLng(37.52623683, 126.93361226), new LatLng( 37.52622713, 126.93362838));
        addDotBrailleBlock(naverMap, new LatLng(37.52623836, 126.93364102), new LatLng( 37.52622713, 126.93362838));
        addDotBrailleBlock(naverMap, new LatLng(37.52621122, 126.93364469), new LatLng( 37.52622713, 126.93362838));

        addDotBrailleBlock(naverMap, new LatLng(37.52601374, 126.93390651), new LatLng( 37.52599541, 126.93392535));
        addDotBrailleBlock(naverMap, new LatLng(37.52601184, 126.93394634), new LatLng( 37.52599541, 126.93392535));
        addDotBrailleBlock(naverMap, new LatLng(37.52604918, 126.93425169), new LatLng(  37.52604725, 126.93428587));
        addDotBrailleBlock(naverMap, new LatLng( 37.52607395, 126.93426738), new LatLng( 37.52604725, 126.93428587));

        // 장애물 좌표
        // 37.52754974, 126.93289687
        // 37.52680387, 126.93437803

        // 장애물 좌표 설정 (예시 좌표 추가)
        LatLng obstaclePosition1 = new LatLng(37.52754974, 126.93289687); // 장애물 1
        LatLng obstaclePosition2 = new LatLng(37.52680387, 126.93437803); // 장애물 2

        // 화장실 위치
        // 37.52623836, 126.93364102

        // 교차로 위치
        // 37.52748075, 126.93280613
        // 37.52676205, 126.93366983
        // 37.52657932, 126.93327844
        // 37.52715291, 126.93237505
        // 37.52643553, 126.93396365
        // 37.52654361, 126.93320511
        // 37.52622713, 126.93362838
        // 37.52599541, 126.93392535
        // 37.52604725, 126.93428587

        // 위치 변경 리스너 추가
        naverMap.addOnLocationChangeListener(location -> {
            LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());

            // 점자블록 경계를 벗어났는지 확인
            if (isUserOutsideBrailleBlocks(userPosition)) {
                sendVibrationSignal(); // 진동 신호 전송
            }
            if (isUserOnDotBrailleBlock(userPosition)) {
                sendRepeatedVibrationSignal(5); // 진동 신호 5번 반복 전송
            }
            // 장애물 앞 진동 트리거
            triggerObstacleVibration(userPosition, obstaclePosition1); // 장애물 1에 대한 진동
            triggerObstacleVibration(userPosition, obstaclePosition2); // 장애물 2에 대한 진동
            // 진동 작동 트리거
            triggerVibrationAtIntersection(userPosition);
        });
    }

    // 선형 점자블록 추가 함수
    private void addLinearBrailleBlock(NaverMap naverMap, LatLng startPoint, LatLng endPoint) {
        PolylineOverlay polyline = new PolylineOverlay();
        polyline.setCoords(Arrays.asList(startPoint, endPoint));
        polyline.setWidth(10); // 선의 두께 설정 (단위: 픽셀)
        polyline.setColor(0xFFFFA500); // 주황색 선
        polyline.setMap(naverMap);

        // 점자블록 리스트에 추가
        brailleBlocks.add(polyline);
    }

    // 점형 점자블록 추가 함수
    private void addDotBrailleBlock(NaverMap naverMap, LatLng startPoint, LatLng endPoint) {
        PolylineOverlay polyline = new PolylineOverlay();
        polyline.setCoords(Arrays.asList(startPoint, endPoint));
        polyline.setWidth(10); // 선의 두께 설정 (단위: 픽셀)
        polyline.setColor(0xFFFF00A5); // 핑크색 선
        polyline.setMap(naverMap);
    }
    // 장애물 앞 진동 함수 추가
    private void triggerObstacleVibration(LatLng userPosition, LatLng obstaclePosition) {
        double distance = distance(userPosition, obstaclePosition);

        // 장애물과의 거리가 5미터 이내일 때 진동 5번 울림
        if (distance <= 0.005) {
            sendRepeatedVibrationSignal(5); // 진동 신호 5번 반복 전송
        }
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
}
/////////////////////////////////////////////////////////////////////////////

