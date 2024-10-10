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
import com.naver.maps.map.overlay.CircleOverlay;

import java.util.List;

public class SectionManager {

    private Context context;
    private final ArduinoVibrationController avc = new ArduinoVibrationController();
    private OutputStream outputStream; // 아두이노로 데이터를 전송할 OutputStream
    private boolean isUserNearSection = true; // 장애물 근처 상태를 추적하는 플래그

    // 좌표별로 case 번호를 지정하기 위한 Map 정의
    private final Map<Integer, List<LatLng>> coordinateCases = new HashMap<>(); // Map<Integer, List<LatLng>>
    private final LatLng section1 = new LatLng(37.51997367, 127.09847681);
    private final LatLng section2 = new LatLng(37.51995139, 127.09843472);
    private final LatLng section3 = new LatLng(37.51995441, 127.09845953);


    // 생성자에서 좌표와 case 번호를 매핑

    public SectionManager(Context context, OutputStream outputStream) {
        this.context = context;
        this.outputStream = outputStream;

        // case 1
        List<LatLng> case1Coords = Arrays.asList(
                new LatLng(37.52749844, 126.93282825),
                new LatLng(37.52673475, 126.93369419),
                new LatLng(37.52659294, 126.93330747),
                new LatLng(37.52655245, 126.93318723),
                new LatLng(37.52623683, 126.93361226),
                new LatLng(37.52601374, 126.93390651)
        );
        coordinateCases.put(1, case1Coords);
        // case 2 좌표 그룹화
        List<LatLng> case2Coords = Arrays.asList(

                new LatLng(37.52746540, 126.93278579),
                new LatLng(37.52713827, 126.93239991),
                new LatLng(37.52677942, 126.93364775),
                new LatLng(37.52656957, 126.93325711),
                new LatLng(37.52653469, 126.93323310),
                new LatLng(37.52607395, 126.93426738)
        );
        coordinateCases.put(2, case2Coords);
        // case 3 좌표 그룹화
        List<LatLng> case3Coords = Arrays.asList(

                new LatLng(37.52746078, 126.93283010),
                new LatLng(37.52719621, 126.93243238),
                new LatLng(37.52674941, 126.93364060),
                new LatLng(37.52657473, 126.93330811),
                new LatLng(37.52656118, 126.93323845),
                new LatLng(37.52601184, 126.93394634)
        );
        coordinateCases.put(3, case3Coords);
        // case 4 좌표 그룹화
        List<LatLng> case4Coords = Arrays.asList(
                new LatLng(37.52645537, 126.93398751),
                new LatLng(37.52644432, 126.93393378),
                new LatLng(37.52640692, 126.93398781)
        );
        coordinateCases.put(4, case4Coords);
    }
    public SectionManager(Context context){this.context = context;}

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
    // 교차로 위치에 원을 표시하는 함수
    private void addCrossroadCircle(NaverMap naverMap, LatLng crossroad) {
        CircleOverlay circle = new CircleOverlay();
        circle.setCenter(crossroad);
        circle.setRadius(1.0); // 반경 1.5미터
        circle.setColor(0x4000FF00); // 초록색 반투명 원 (0x40은 투명도)
        circle.setOutlineColor(0xFF00FF00); // 초록색 테두리
        circle.setOutlineWidth(3); // 테두리 두께
        circle.setMap(naverMap);
    }
    public void addSectiononMap(@NonNull NaverMap naverMap) {

        // 선형 점자블록 추가
        addLinearBrailleBlock(naverMap, new LatLng(37.52761704, 126.93297516), new LatLng(37.5284726, 126.93409373));
        addLinearBrailleBlock(naverMap, new LatLng(37.52847627, 126.93413048), new LatLng(37.52758931, 126.93520385));
        addLinearBrailleBlock(naverMap, new LatLng(37.52755322, 126.9352127), new LatLng(37.527326, 126.93496173));
        addLinearBrailleBlock(naverMap, new LatLng(37.5272937, 126.93492551), new LatLng(37.52685604, 126.93443723));
        addLinearBrailleBlock(naverMap, new LatLng(37.52729597, 126.93496488), new LatLng(37.52706028, 126.935253));
        addLinearBrailleBlock(naverMap, new LatLng(37.52706028, 126.935253), new LatLng(37.52690819, 126.93550214));
        addLinearBrailleBlock(naverMap, new LatLng(37.52690819, 126.93550214), new LatLng(37.5266179, 126.93589983));
        // 3번째 37.52627961 126.93522186

        addLinearBrailleBlock(naverMap, new LatLng(37.52577618, 126.93423173), new LatLng(37.52589178, 126.93445328));
        addLinearBrailleBlock(naverMap, new LatLng(37.52589178, 126.93445328), new LatLng(37.5262675, 126.93518912));
        addLinearBrailleBlock(naverMap, new LatLng(37.526604, 126.9359167), new LatLng(37.52627961, 126.93522186));
        addLinearBrailleBlock(naverMap, new LatLng(37.52589013, 126.93445074), new LatLng(37.52509846, 126.9357385));
        addLinearBrailleBlock(naverMap, new LatLng(37.526604, 126.9359167), new LatLng(37.52571447, 126.93679827));
        addLinearBrailleBlock(naverMap, new LatLng(37.52491457, 126.9354831), new LatLng(37.52509757, 126.93573975));
        addLinearBrailleBlock(naverMap, new LatLng(37.52509898, 126.9357415), new LatLng(37.52526247, 126.9359579));
        addLinearBrailleBlock(naverMap, new LatLng(37.52526212, 126.93595801), new LatLng(37.52571529, 126.93679664));
        addLinearBrailleBlock(naverMap, new LatLng(37.5250992, 126.93574015), new LatLng(37.52483355, 126.93604164));
        addLinearBrailleBlock(naverMap, new LatLng(37.52441175, 126.93600736), new LatLng(37.52510821, 126.93609407));

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

// 교차로 위치에 원을 표시
        addCrossroadCircle(naverMap, section1);  // 교차로 1
        addCrossroadCircle(naverMap, section2);  // 교차로 2
        addCrossroadCircle(naverMap, section3); // 교차로 3

        // 화장실 위치
        // 37.52623836, 126.93364102
        // 위치 변경 리스너 추가
        naverMap.addOnLocationChangeListener(location -> {
            LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());
            checkUserNearCrossroad(userPosition); // 교차로 근처 확인
        });
    }
    // 사용자와 교차로 간의 거리를 확인하고 알림을 보내는 함수
    private void checkUserNearCrossroad(LatLng userPosition) {
            double thresholdDistance = 3.0; // 2m 이내일 때 장애물 근처로 간주

            if (distanceBetween(userPosition, section1) < thresholdDistance ){
                Toast.makeText(context, "교차로1 앞에 있습니다.", Toast.LENGTH_SHORT).show();
            }
            if (distanceBetween(userPosition, section2) < thresholdDistance ){
            Toast.makeText(context, "교차로2 앞에 있습니다.", Toast.LENGTH_SHORT).show();
            }

            if (distanceBetween(userPosition, section3) < thresholdDistance ){
            Toast.makeText(context, "교차로3 앞에 있습니다.", Toast.LENGTH_SHORT).show();
        }
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


    // 아두이노로 테스트 명령어를 전송하는 함수 (7번 명령어 전송)
    public void sendTestCommandToArduino() {
        try {
            if (outputStream != null) {
                outputStream.write(("7\n").getBytes());  // 명령어 7을 아두이노로 전송
                outputStream.flush();
                Toast.makeText(context, "Command 7 sent to Arduino", Toast.LENGTH_SHORT).show();  // 디버깅용 메시지
            } else {
                Toast.makeText(context, "OutputStream is null", Toast.LENGTH_SHORT).show();  // 디버깅용 메시지
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to send command", Toast.LENGTH_SHORT).show();  // 오류 처리
        }
    }

    /*// case 번호에 따른 동작 처리 함수 // 아두이노로 case 번호 전송
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
            case 6:
                avc.sendRepeatedVibrationSignal(6);
                Toast.makeText(context, "Case 6: 장애물 앞에 있습니다", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }*/

}
