package com.chungangpark.parknavigator;

import androidx.annotation.NonNull;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.PolylineOverlay;
import java.util.Arrays;
import java.io.IOException;
import java.io.OutputStream;
import android.content.Context;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class BrailleBlockManager {
    private Context context;
    private final BrailleBlockDetector bbd = new BrailleBlockDetector();
    private final ArduinoVibrationController avc = new ArduinoVibrationController();
    private OutputStream outputStream; // 아두이노로 데이터를 전송할 OutputStream
    // 새로운 변수 추가
    private boolean wasOnBrailleBlock = false; // 사용자가 처음에는 점자블록 위에 없다고 가정
    // 좌표별로 case 번호를 지정하기 위한 Map 정의
    private final Map<LatLng, Integer> coordinateCases = new HashMap<>();
    private boolean wasOnLinearBlock = false; // 이전에 선형 점자블록에 있었는지 확인하는 플래그
    // 장애물 좌표 정의
    private final LatLng obstacle1 = new LatLng(37.52754974, 126.93289687);
    private final LatLng obstacle2 = new LatLng(37.52680387, 126.93437803);

    // 생성자에서 좌표와 case 번호를 매핑
    public BrailleBlockManager(Context context, OutputStream outputStream) {
        this.context = context;
        this.outputStream = outputStream;
        // case 1
        coordinateCases.put(new LatLng(37.52749844, 126.93282825), 1);
        coordinateCases.put(new LatLng(37.52673475, 126.93369419), 1);
        coordinateCases.put(new LatLng(37.52659294, 126.93330747), 1);
        coordinateCases.put(new LatLng(37.52655245, 126.93318723), 1);
        coordinateCases.put(new LatLng(37.52623683, 126.93361226), 1);
        coordinateCases.put(new LatLng(37.52601374, 126.93390651), 1);

        // case 2
        coordinateCases.put(new LatLng(37.52746540, 126.93278579), 2);
        coordinateCases.put(new LatLng(37.52713827, 126.93239991), 2);
        coordinateCases.put(new LatLng(37.52677942, 126.93364775), 2);
        coordinateCases.put(new LatLng(37.52656957, 126.93325711), 2);
        coordinateCases.put(new LatLng(37.52653469, 126.93323310), 2);
        coordinateCases.put(new LatLng(37.52621122, 126.93364469), 2);
        coordinateCases.put(new LatLng(37.52607395, 126.93426738), 2);

        // case 3
        coordinateCases.put(new LatLng(37.52746078, 126.93283010), 3);
        coordinateCases.put(new LatLng(37.52719621, 126.93243238), 3);
        coordinateCases.put(new LatLng(37.52674941, 126.93364060), 3);
        coordinateCases.put(new LatLng(37.52657473, 126.93330811), 3);
        coordinateCases.put(new LatLng(37.52656118, 126.93323845), 3);
        coordinateCases.put(new LatLng(37.52604918, 126.93425169), 3);
        coordinateCases.put(new LatLng(37.52601184, 126.93394634), 3);
        coordinateCases.put(new LatLng(37.52623836, 126.93364102), 3);

        // case 4
        coordinateCases.put(new LatLng(37.52645537, 126.93398751), 4);
        coordinateCases.put(new LatLng(37.52644432, 126.93393378), 4);
        coordinateCases.put(new LatLng(37.52640692, 126.93398781), 4);

        // 장애물 좌표 case 6으로 추가
        coordinateCases.put(obstacle1, 6);
        coordinateCases.put(obstacle2, 6);
    }
    public BrailleBlockManager(Context context){this.context = context;}
    // 선형 점자블록 추가 함수
    private void addLinearBrailleBlock(NaverMap naverMap, LatLng startPoint, LatLng endPoint) {
        PolylineOverlay polyline = new PolylineOverlay();
        polyline.setCoords(Arrays.asList(startPoint, endPoint));
        polyline.setWidth(10); // 선의 두께 설정 (단위: 픽셀)
        polyline.setColor(0xFFFFA500); // 주황색 선
        polyline.setMap(naverMap);

        // 점자블록 리스트에 추가
        MainActivity.brailleBlocks.add(polyline);
    }

    // 점형 점자블록 추가 함수
    private void addDotBrailleBlock(NaverMap naverMap, LatLng startPoint, LatLng endPoint) {
        PolylineOverlay polyline = new PolylineOverlay();
        polyline.setCoords(Arrays.asList(startPoint, endPoint));
        polyline.setWidth(10); // 선의 두께 설정 (단위: 픽셀)
        polyline.setColor(0xFFFF00A5); // 핑크색 선
        polyline.setMap(naverMap);
    }

    public void addBrailleBlockonMap(@NonNull NaverMap naverMap) {

        // 선형 점자블록 추가
        addLinearBrailleBlock(naverMap, new LatLng(37.52709831, 126.93245824), new LatLng(37.52713827, 126.93239991));
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
        addLinearBrailleBlock(naverMap, new LatLng(37.52645537, 126.93398751), new LatLng(37.52678618, 126.93435865));

        addLinearBrailleBlock(naverMap, new LatLng(37.52672839, 126.93294539), new LatLng(37.52655245, 126.93318723));

        addLinearBrailleBlock(naverMap, new LatLng(37.52653469, 126.93323310), new LatLng(37.52623683, 126.93361226));
        addLinearBrailleBlock(naverMap, new LatLng(37.52621122, 126.93364469), new LatLng(37.52601374, 126.93390651));

        addLinearBrailleBlock(naverMap, new LatLng(37.52601184, 126.93394634), new LatLng(37.52605962, 126.93401307));
        addLinearBrailleBlock(naverMap, new LatLng(37.52606486, 126.93406153), new LatLng(37.52605962, 126.93401307));
        addLinearBrailleBlock(naverMap, new LatLng(37.52604918, 126.93425169), new LatLng(37.52606486, 126.93406153));

        // 점형 점자블록 추가
        addDotBrailleBlock(naverMap, new LatLng(37.52746540, 126.93278579), new LatLng(37.52748075, 126.93280613));
        addDotBrailleBlock(naverMap, new LatLng(37.52746078, 126.93283010), new LatLng(37.52748075, 126.93280613));
        addDotBrailleBlock(naverMap, new LatLng(37.52749844, 126.93282825), new LatLng(37.52748075, 126.93280613));
        addDotBrailleBlock(naverMap, new LatLng(37.5666102, 126.9783881), new LatLng(37.5668202, 126.9786881));
        addDotBrailleBlock(naverMap, new LatLng(37.52754974, 126.93289687), new LatLng(37.52753334, 126.93287541));

        addDotBrailleBlock(naverMap, new LatLng(37.52677942, 126.93364775), new LatLng(37.52676205, 126.93366983));
        addDotBrailleBlock(naverMap, new LatLng(37.52674941, 126.93364060), new LatLng(37.52676205, 126.93366983));
        addDotBrailleBlock(naverMap, new LatLng(37.52673475, 126.93369419), new LatLng(37.52676205, 126.93366983));

        addDotBrailleBlock(naverMap, new LatLng(37.52656957, 126.93325711), new LatLng(37.52657932, 126.93327844));
        addDotBrailleBlock(naverMap, new LatLng(37.52659294, 126.93330747), new LatLng(37.52657932, 126.93327844));
        addDotBrailleBlock(naverMap, new LatLng(37.52657473, 126.93330811), new LatLng(37.52657932, 126.93327844));
        addDotBrailleBlock(naverMap, new LatLng(37.52657473, 126.93330811), new LatLng(37.52657932, 126.93327844));


        addDotBrailleBlock(naverMap, new LatLng(37.52656118, 126.93323845), new LatLng(37.52654361, 126.93320511));

        addDotBrailleBlock(naverMap, new LatLng(37.52719621, 126.93243238), new LatLng(37.52715291, 126.93237505));
        addDotBrailleBlock(naverMap, new LatLng(37.52713827, 126.93239991), new LatLng(37.52715291, 126.93237505));

        addDotBrailleBlock(naverMap, new LatLng(37.52646021, 126.93394120), new LatLng(37.52643553, 126.93396365));
        addDotBrailleBlock(naverMap, new LatLng(37.52644432, 126.93393378), new LatLng(37.52643553, 126.93396365));
        addDotBrailleBlock(naverMap, new LatLng(37.52640692, 126.93398781), new LatLng(37.52643553, 126.93396365));
        addDotBrailleBlock(naverMap, new LatLng(37.52645537, 126.93398751), new LatLng(37.52643553, 126.93396365));
        addDotBrailleBlock(naverMap, new LatLng(37.52678618, 126.93435865), new LatLng(37.52680387, 126.93437803));

        addDotBrailleBlock(naverMap, new LatLng(37.52655245, 126.93318723), new LatLng(37.52654361, 126.93320511));
        addDotBrailleBlock(naverMap, new LatLng(37.52653469, 126.93323310), new LatLng(37.52654361, 126.93320511));


        addDotBrailleBlock(naverMap, new LatLng(37.52623683, 126.93361226), new LatLng(37.52622713, 126.93362838));
        addDotBrailleBlock(naverMap, new LatLng(37.52623836, 126.93364102), new LatLng(37.52622713, 126.93362838));
        addDotBrailleBlock(naverMap, new LatLng(37.52621122, 126.93364469), new LatLng(37.52622713, 126.93362838));

        addDotBrailleBlock(naverMap, new LatLng(37.52601374, 126.93390651), new LatLng(37.52599541, 126.93392535));
        addDotBrailleBlock(naverMap, new LatLng(37.52601184, 126.93394634), new LatLng(37.52599541, 126.93392535));
        addDotBrailleBlock(naverMap, new LatLng(37.52604918, 126.93425169), new LatLng(37.52604725, 126.93428587));
        addDotBrailleBlock(naverMap, new LatLng(37.52607395, 126.93426738), new LatLng(37.52604725, 126.93428587));

        // 테스트 좌표 ///////////////////////////////////////////
        addLinearBrailleBlock(naverMap, new LatLng(37.52064917, 127.09412791), new LatLng(37.52070961, 127.09409197));
        addDotBrailleBlock(naverMap, new LatLng(37.52070670, 127.09406298), new LatLng(37.52070961, 127.09409197));

        ////////////////////////////////////////////////////////
        // 장애물 좌표
        // 37.52754974, 126.93289687
        // 37.52680387, 126.93437803

        // 화장실 위치
        // 37.52623836, 126.93364102
        // 위치 변경 리스너 추가
        naverMap.addOnLocationChangeListener(location -> {
            LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());

            // 사용자가 선형 또는 점형 점자블록 위에 있는지 확인
            boolean isOnLinearBlock = !bbd.isUserOutsideBrailleBlocks(userPosition); // 선형 점자블록 위에 있는지 확인
            boolean isOnDotBlock = bbd.isUserOnDotBrailleBlock(userPosition); // 점형 점자블록 위에 있는지 확인
            boolean isOnBrailleBlock = isOnLinearBlock || isOnDotBlock; // 둘 중 하나라도 true이면 점자블록 위에 있음

            // 장애물에 도달했는지 확인
            if (isNearObstacle(userPosition)) {
                handleCase(6);  // case 6: 장애물에 도달했을 때
            }
            // 선형 점자블록에서 점형 점자블록으로 이동 시
            if (isOnLinearBlock && !wasOnLinearBlock) {
                LatLng nearestCoordinate = bbd.getNearestBrailleBlock(userPosition);
                if (nearestCoordinate != null) {
                    int caseNumber = coordinateCases.getOrDefault(nearestCoordinate, -1);
                    handleCase(caseNumber);  // case 번호에 따른 동작 처리
                }
            }

            // 3. 점자블록에서 벗어났을 때 처리
            if (!isOnBrailleBlock && wasOnBrailleBlock) {
                handleCase(5); // case 5: 점자블록에서 벗어났을 때
            }

            // 점자블록 경계를 벗어났는지 확인 (테스트용)
            if (!isOnBrailleBlock) {
                avc.sendVibrationSignal(); // 진동 신호 전송
                Toast.makeText(context, "선형 점자블록을 벗어났습니다", Toast.LENGTH_SHORT).show();
            }

            // 장애물 앞에 있을 때 진동(해당 위치에 도달했을 때 진동)(테스트용)
            if (isOnDotBlock) {
                avc.sendRepeatedVibrationSignal(5); // 진동 신호 5번 반복 전송
                Toast.makeText(context, "진동이 울립니다", Toast.LENGTH_SHORT).show();
            }

            // 6. 상태 업데이트: 현재 점자블록 상태를 기록
            wasOnBrailleBlock = isOnBrailleBlock;
        });

    }
    // 장애물에 도달했는지 확인하는 함수
    private boolean isNearObstacle(LatLng userPosition) {
        double thresholdDistance = 0.00005; // 장애물 근처로 간주할 거리 기준
        return (bbd.distance(userPosition, obstacle1) < thresholdDistance) ||
                (bbd.distance(userPosition, obstacle2) < thresholdDistance);
    }
    // case 번호에 따른 동작 처리 함수 // 아두이노로 case 번호 전송
    private void handleCase(int caseNumber) {
        try {
            if (outputStream != null) {
                outputStream.write((caseNumber + "\n").getBytes()); // case 번호를 아두이노로 전송
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 디버깅
        switch (caseNumber) {
            case 1:
                avc.sendVibrationSignal();
                Toast.makeText(context, "Case 1: 아두이노로 1 전송", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                avc.sendRepeatedVibrationSignal(2);
                Toast.makeText(context, "Case 2: 아두이노로 2 전송", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                avc.sendRepeatedVibrationSignal(3);
                Toast.makeText(context, "Case 3: 아두이노로 3 전송", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                avc.sendRepeatedVibrationSignal(4);
                Toast.makeText(context, "Case 4: 아두이노로 4 전송", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                avc.sendRepeatedVibrationSignal(5);
                Toast.makeText(context, "Case 5: 선형 점자블록에서 벗어났습니다", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
