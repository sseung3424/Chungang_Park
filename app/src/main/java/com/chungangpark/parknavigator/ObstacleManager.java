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

import java.util.ArrayList;
import java.util.List;

public class ObstacleManager {

    private Context context;
    private final ArduinoVibrationController avc = new ArduinoVibrationController();
    private OutputStream outputStream; // 아두이노로 데이터를 전송할 OutputStream

    // 좌표별로 case 번호를 지정하기 위한 Map 정의
    private final Map<LatLng, Integer> coordinateCases = new HashMap<>();
    // 장애물 좌표 정의
    private final LatLng obstacle1 = new LatLng(37.52754974, 126.93289687);
    private final LatLng obstacle2 = new LatLng(37.52758983, 126.93294895);
    private final LatLng obstacle3 = new LatLng(37.52680175, 126.93437625);
    private final LatLng obstacle4 = new LatLng(37.52684741, 126.93442565);
    private final LatLng obstacle5 = new LatLng(37.52628100, 126.93522377);
    private final LatLng obstacle6 = new LatLng(37.52524093, 126.93592885);
    private final LatLng obstacle7 = new LatLng(37.52528051, 126.93598924);
    private final LatLng obstacle8 = new LatLng(37.58498274, 126.88564608);
    private final LatLng obstacle9 = new LatLng(37.52624885, 126.9351565);

    private boolean isUserNearObstacle = true; // 장애물 근처 상태를 추적하는 플래그

    // 생성자에서 좌표와 case 번호를 매핑

    public ObstacleManager(Context context, OutputStream outputStream) {
        this.context = context;
        this.outputStream = outputStream;
    }
    public ObstacleManager(Context context){this.context = context;}

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
    // 장애물 위치에 1m 반경의 원을 표시하는 함수
    private void addObstacleCircle(NaverMap naverMap, LatLng obstacle) {
        CircleOverlay circle = new CircleOverlay();
        circle.setCenter(obstacle);
        circle.setRadius(1.0); // 1.5미터 반경
        circle.setColor(0x40FF0000); // 반투명 빨간색
        circle.setOutlineColor(0xFFFF0000); // 빨간색 테두리
        circle.setOutlineWidth(3); // 테두리 두께
        circle.setMap(naverMap);
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
        addLinearBrailleBlock(naverMap, new LatLng(37.52604918, 126.93425169), new LatLng(37.52606486, 126.93406153));


        // 점자 블록을 추가하는 함수 호출
        addLinearBrailleBlock(naverMap, new LatLng(37.52604151, 126.93429185), new LatLng(37.52590086, 126.93444264));
        addLinearBrailleBlock(naverMap, new LatLng(37.52759299, 126.93519875), new LatLng(37.52757111, 126.93522409));
        addLinearBrailleBlock(naverMap, new LatLng(37.52757111, 126.93522409), new LatLng(37.52754944, 126.93520996));
        addLinearBrailleBlock(naverMap, new LatLng(37.525793, 126.93419373), new LatLng(37.525793, 126.93419373));
        addLinearBrailleBlock(naverMap, new LatLng(37.52575046, 126.93425274), new LatLng(37.52484877, 126.93543505));
        addLinearBrailleBlock(naverMap, new LatLng(37.52480729, 126.93549137), new LatLng(37.52442853, 126.93597018));
        addLinearBrailleBlock(naverMap, new LatLng(37.52438775, 126.93603009), new LatLng(37.52426552, 126.93621893));
        addLinearBrailleBlock(naverMap, new LatLng(37.52426445, 126.93628285), new LatLng(37.52447562, 126.93656391));
        addLinearBrailleBlock(naverMap, new LatLng(37.52452029, 126.93654201), new LatLng(37.52456973, 126.93641224));
        addLinearBrailleBlock(naverMap, new LatLng(37.52456973, 126.93641224), new LatLng(37.52483102, 126.93604008));

        addLinearBrailleBlock(naverMap, new LatLng(37.52970628, 126.92893986), new LatLng(37.52982255, 126.92921813));
        addLinearBrailleBlock(naverMap, new LatLng(37.52982255, 126.92921813), new LatLng(37.52988054, 126.92963528));
        addLinearBrailleBlock(naverMap, new LatLng(37.52988054, 126.92963528), new LatLng(37.52992603, 126.92967059));
        addLinearBrailleBlock(naverMap, new LatLng(37.52992603, 126.92967059), new LatLng(37.52996378, 126.92951125));

        addLinearBrailleBlock(naverMap, new LatLng(37.52995919, 126.92933566), new LatLng(37.52993163, 126.92909854));
        addLinearBrailleBlock(naverMap, new LatLng(37.52993163, 126.92909854), new LatLng(37.53000635, 126.92877467));
        addLinearBrailleBlock(naverMap, new LatLng(37.53006971, 126.92887327), new LatLng(37.53009666, 126.92906566));
        addLinearBrailleBlock(naverMap, new LatLng(37.53009666, 126.92906566), new LatLng(37.53036672, 126.9299682));
        addLinearBrailleBlock(naverMap, new LatLng(37.53036672, 126.9299682), new LatLng(37.53035695, 126.93023189));
        addLinearBrailleBlock(naverMap, new LatLng(37.53035695, 126.93023189), new LatLng(37.53022484, 126.93054601));
        addLinearBrailleBlock(naverMap, new LatLng(37.53002605, 126.92959132), new LatLng(37.53007834, 126.92977587));
        addLinearBrailleBlock(naverMap, new LatLng(37.53007834, 126.92977587), new LatLng(37.53008724, 126.93002461));
        addLinearBrailleBlock(naverMap, new LatLng(37.53008724, 126.93002461), new LatLng(37.53003648, 126.93011342));
        addLinearBrailleBlock(naverMap, new LatLng(37.5310383, 126.93019147), new LatLng(37.53092901, 126.93043326));
        addLinearBrailleBlock(naverMap, new LatLng(37.53085821, 126.93058224), new LatLng(37.53055962, 126.93112349));
        addLinearBrailleBlock(naverMap, new LatLng(37.5308313, 126.9305119), new LatLng(37.53066506, 126.93063718));
        addLinearBrailleBlock(naverMap, new LatLng(37.53066506, 126.93063718), new LatLng(37.53041431, 126.93088871));
        addLinearBrailleBlock(naverMap, new LatLng(37.53033926, 126.93174914), new LatLng(37.53022104, 126.9319281));
        addLinearBrailleBlock(naverMap, new LatLng(37.53017522, 126.93193162), new LatLng(37.53015238, 126.93190579));
        addLinearBrailleBlock(naverMap, new LatLng(37.53015238, 126.93190579), new LatLng(37.53016645, 126.93192234));
        addLinearBrailleBlock(naverMap, new LatLng(37.53018569, 126.93198975), new LatLng(37.53014974, 126.93207503));
        addLinearBrailleBlock(naverMap, new LatLng(37.53014974, 126.93207503), new LatLng(37.52999548, 126.93224066));
        addLinearBrailleBlock(naverMap, new LatLng(37.52999548, 126.93224066), new LatLng(37.5299238, 126.93244069));
        addLinearBrailleBlock(naverMap, new LatLng(37.5299238, 126.93244069), new LatLng(37.52989503, 126.93249793));
        addLinearBrailleBlock(naverMap, new LatLng(37.52989503, 126.93249793), new LatLng(37.52984909, 126.93256066));
        addLinearBrailleBlock(naverMap, new LatLng(37.52984909, 126.93256066), new LatLng(37.52976892, 126.93263829));
        addLinearBrailleBlock(naverMap, new LatLng(37.52976892, 126.93263829), new LatLng(37.5297182, 126.93266677));
        addLinearBrailleBlock(naverMap, new LatLng(37.5297182, 126.93266677), new LatLng(37.52964525, 126.93276415));
        addLinearBrailleBlock(naverMap, new LatLng(37.52964525, 126.93276415), new LatLng(37.52952463, 126.93304594));
        addLinearBrailleBlock(naverMap, new LatLng(37.52952463, 126.93304594), new LatLng(37.52947353, 126.93310569));
        addLinearBrailleBlock(naverMap, new LatLng(37.52947353, 126.93310569), new LatLng(37.52934838, 126.93319514));
        addLinearBrailleBlock(naverMap, new LatLng(37.52934838, 126.93319514), new LatLng(37.52921033, 126.93327319));
        addLinearBrailleBlock(naverMap, new LatLng(37.52921033, 126.93327319), new LatLng(37.52918223, 126.93328131));
        addLinearBrailleBlock(naverMap, new LatLng(37.53010115, 126.93191484), new LatLng(37.52994823, 126.93208164));
        addLinearBrailleBlock(naverMap, new LatLng(37.52994823, 126.93208164), new LatLng(37.52979686, 126.93235946));
        addLinearBrailleBlock(naverMap, new LatLng(37.52979686, 126.93235946), new LatLng(37.52917219, 126.93325816));
        addLinearBrailleBlock(naverMap, new LatLng(37.52912681, 126.93331917), new LatLng(37.52851631, 126.93408292));

        addLinearBrailleBlock(naverMap, new LatLng(37.58507623, 126.88571436), new LatLng(37.58500977, 126.88566657));










        addLinearBrailleBlock(naverMap, new LatLng(37.58494730, 126.88573480), new LatLng(37.58495490, 126.88571960));

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

        addDotBrailleBlock(naverMap, new LatLng(37.52599421, 126.93392372), new LatLng(37.5260169, 126.93390002));
        addDotBrailleBlock(naverMap, new LatLng(37.52599421, 126.93392372), new LatLng(37.52601974, 126.93395501));
        addDotBrailleBlock(naverMap, new LatLng(37.52599421, 126.93392372), new LatLng(37.52597613, 126.93394964));

        addDotBrailleBlock(naverMap, new LatLng(37.52577137, 126.93422547), new LatLng(37.525793, 126.93419373));
        addDotBrailleBlock(naverMap, new LatLng(37.52577137, 126.93422547), new LatLng(37.52578981, 126.93425453));
        addDotBrailleBlock(naverMap, new LatLng(37.52577137, 126.93422547), new LatLng(37.52575046, 126.93425274));

        addDotBrailleBlock(naverMap, new LatLng(37.52482892, 126.93546366), new LatLng(37.52484877, 126.93543505));
        addDotBrailleBlock(naverMap, new LatLng(37.52482892, 126.93546366), new LatLng(37.52486118, 126.93547215));
        addDotBrailleBlock(naverMap, new LatLng(37.52482892, 126.93546366), new LatLng(37.52480729, 126.93549137));

        addDotBrailleBlock(naverMap, new LatLng(37.52440654, 126.93600147), new LatLng(37.52442853, 126.93597018));
        addDotBrailleBlock(naverMap, new LatLng(37.52440654, 126.93600147), new LatLng(37.52443562, 126.93602472));
        addDotBrailleBlock(naverMap, new LatLng(37.52440654, 126.93600147), new LatLng(37.52438775, 126.93603009));

        addDotBrailleBlock(naverMap, new LatLng(37.52423999, 126.93625514), new LatLng(37.52426552, 126.93621893));
        addDotBrailleBlock(naverMap, new LatLng(37.52423999, 126.93625514), new LatLng(37.52426445, 126.93628285));
        addDotBrailleBlock(naverMap, new LatLng(37.52423999, 126.93625514), new LatLng(37.52420808, 126.9362985));

        addDotBrailleBlock(naverMap, new LatLng(37.52450185, 126.9365961), new LatLng(37.52452029, 126.93654201));
        addDotBrailleBlock(naverMap, new LatLng(37.52450185, 126.9365961), new LatLng(37.52446108, 126.93661935));
        addDotBrailleBlock(naverMap, new LatLng(37.52450185, 126.9365961), new LatLng(37.52451462, 126.93661622));
        addDotBrailleBlock(naverMap, new LatLng(37.52450185, 126.9365961), new LatLng(37.52447562, 126.93656391));



        // 장애물 위치에 1m 반경의 원 추가
        addObstacleCircle(naverMap, obstacle1);
        addObstacleCircle(naverMap, obstacle2);
        addObstacleCircle(naverMap, obstacle3);
        addObstacleCircle(naverMap, obstacle4);
        addObstacleCircle(naverMap, obstacle5);
        addObstacleCircle(naverMap, obstacle6);
        addObstacleCircle(naverMap, obstacle7);
        addObstacleCircle(naverMap, obstacle8);
        // 화장실 위치
        // 37.52623836, 126.93364102
        // 위치 변경 리스너 추가
        naverMap.addOnLocationChangeListener(location -> {
            LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());

            checkUserNearObstacle(userPosition);
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

    private void checkUserNearObstacle(LatLng userPosition) {
        double thresholdDistance = 3.0; // 3m 이내일 때 장애물 근처로 간주

        boolean isNearObstacle = false;

        // 장애물들과의 거리 계산
        if (distanceBetween(userPosition, obstacle1) < thresholdDistance ||
                distanceBetween(userPosition, obstacle2) < thresholdDistance ||
                distanceBetween(userPosition, obstacle3) < thresholdDistance) {

            isNearObstacle = true;
        }


        if (distanceBetween(userPosition, obstacle1) < thresholdDistance || distanceBetween(userPosition, obstacle2) < thresholdDistance
                || distanceBetween(userPosition, obstacle3) < thresholdDistance) {
            if (isUserNearObstacle) { // 처음으로 장애물 근처에 도달했을 때
                Toast.makeText(context, "장애물 앞에 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // 아두이노로 특정 명령어를 전송하는 함수
    private void sendCommandToArduino(int command) {
        try {
            if (outputStream != null) {
                String commandStr = command + "\n";
                outputStream.write(commandStr.getBytes());  // 명령어를 아두이노로 전송
                outputStream.flush();
            } else {
                Toast.makeText(context, "OutputStream is null", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to send command", Toast.LENGTH_SHORT).show();
        }
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
            case 6:
                avc.sendRepeatedVibrationSignal(6);
                Toast.makeText(context, "Case 6: 장애물 앞에 있습니다", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}