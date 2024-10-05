package com.chungangpark.parknavigator;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.PolylineOverlay;

import java.util.ArrayList;
import java.util.List;

public class BrailleBlockDetector {
    NavigationManager nm = new NavigationManager();
    // 클래스에 점자블록 리스트 추가
    private List<PolylineOverlay> brailleBlocks = new ArrayList<>();

}
