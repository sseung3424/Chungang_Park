package com.chungangpark.parknavigator;

import androidx.annotation.NonNull;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.CircleOverlay;
import android.content.Context;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class BrailleBlockManager {
    private Context context;
    private List<LatLng> brailleBlockPoints;
    private boolean isUserNearBrailleBlock = false; // 사용자 상태를 추적하는 플래그

    public BrailleBlockManager(Context context) {
        this.context = context;
        brailleBlockPoints = new ArrayList<>();

        // 세 좌표를 점자블록 리스트에 추가
        brailleBlockPoints.add(new LatLng(37.51999291, 127.09851956));
        brailleBlockPoints.add(new LatLng(37.52000368, 127.09853365));
        brailleBlockPoints.add(new LatLng(37.52001018, 127.09854337));


        brailleBlockPoints.add(new LatLng(37.52710264, 126.93245165));
        brailleBlockPoints.add(new LatLng(37.52713123, 126.9324228));
        brailleBlockPoints.add(new LatLng(37.52715073, 126.9324009));
        brailleBlockPoints.add(new LatLng(37.52716491, 126.93238078));
        brailleBlockPoints.add(new LatLng(37.52717874, 126.93240492));
        brailleBlockPoints.add(new LatLng(37.52720108, 126.93243666));
        brailleBlockPoints.add(new LatLng(37.52722235, 126.93246438));
        brailleBlockPoints.add(new LatLng(37.52724149, 126.9324903));
        brailleBlockPoints.add(new LatLng(37.52726418, 126.93252204));
        brailleBlockPoints.add(new LatLng(37.52729361, 126.93255959));
        brailleBlockPoints.add(new LatLng(37.52732126, 126.93259893));
        brailleBlockPoints.add(new LatLng(37.5273521, 126.93263738));
        brailleBlockPoints.add(new LatLng(37.52737798, 126.93267493));
        brailleBlockPoints.add(new LatLng(37.52740918, 126.93271293));
        brailleBlockPoints.add(new LatLng(37.527434, 126.93274511));
        brailleBlockPoints.add(new LatLng(37.52745244, 126.93277059));
        brailleBlockPoints.add(new LatLng(37.52747193, 126.93279384));
        brailleBlockPoints.add(new LatLng(37.52748044, 126.93280636));
        brailleBlockPoints.add(new LatLng(37.5275003, 126.93283184));
        brailleBlockPoints.add(new LatLng(37.5275244, 126.93286045));
        brailleBlockPoints.add(new LatLng(37.52753469, 126.93287744));
        brailleBlockPoints.add(new LatLng(37.5275478, 126.93289398));
        brailleBlockPoints.add(new LatLng(37.52745492, 126.93283586));
        brailleBlockPoints.add(new LatLng(37.52743684, 126.93285732));
        brailleBlockPoints.add(new LatLng(37.52741734, 126.93288235));
        brailleBlockPoints.add(new LatLng(37.52739429, 126.93291007));
        brailleBlockPoints.add(new LatLng(37.52736239, 126.93294851));
        brailleBlockPoints.add(new LatLng(37.52733899, 126.93297444));
        brailleBlockPoints.add(new LatLng(37.52730885, 126.93301244));
        brailleBlockPoints.add(new LatLng(37.5272851, 126.9330415));
        brailleBlockPoints.add(new LatLng(37.52725957, 126.93307011));
        brailleBlockPoints.add(new LatLng(37.5272383, 126.93309648));
        brailleBlockPoints.add(new LatLng(37.52721419, 126.9331318));
        brailleBlockPoints.add(new LatLng(37.52718193, 126.93316622));
        brailleBlockPoints.add(new LatLng(37.52714081, 126.93321405));
        brailleBlockPoints.add(new LatLng(37.52710784, 126.93325473));
        brailleBlockPoints.add(new LatLng(37.5270738, 126.93329407));
        brailleBlockPoints.add(new LatLng(37.52704047, 126.93333341));
        brailleBlockPoints.add(new LatLng(37.52700291, 126.93337811));
        brailleBlockPoints.add(new LatLng(37.52696709, 126.93342371));
        brailleBlockPoints.add(new LatLng(37.5269327, 126.93346305));
        brailleBlockPoints.add(new LatLng(37.52689937, 126.93350507));
        brailleBlockPoints.add(new LatLng(37.52686853, 126.93354173));
        brailleBlockPoints.add(new LatLng(37.52683875, 126.93357839));
        brailleBlockPoints.add(new LatLng(37.52680755, 126.93361594));
        brailleBlockPoints.add(new LatLng(37.5267799, 126.93364723));
        brailleBlockPoints.add(new LatLng(37.52676146, 126.93367003));
        brailleBlockPoints.add(new LatLng(37.52672732, 126.93294502));
        brailleBlockPoints.add(new LatLng(37.52670073, 126.93297318));
        brailleBlockPoints.add(new LatLng(37.52666634, 126.93301163));
        brailleBlockPoints.add(new LatLng(37.52663656, 126.9330541));
        brailleBlockPoints.add(new LatLng(37.52660962, 126.93309254));
        brailleBlockPoints.add(new LatLng(37.52657913, 126.93313367));
        brailleBlockPoints.add(new LatLng(37.52655254, 126.93317301));
        brailleBlockPoints.add(new LatLng(37.52654686, 126.93320788));
        brailleBlockPoints.add(new LatLng(37.52655007, 126.93321633));
        brailleBlockPoints.add(new LatLng(37.52655565, 126.93322772));
        brailleBlockPoints.add(new LatLng(37.52655992, 126.93323437));
        brailleBlockPoints.add(new LatLng(37.5265655, 126.93324373));
        brailleBlockPoints.add(new LatLng(37.52657037, 126.9332581));
        brailleBlockPoints.add(new LatLng(37.52657264, 126.9332646));
        brailleBlockPoints.add(new LatLng(37.52657898, 126.93327858));
        brailleBlockPoints.add(new LatLng(37.52658644, 126.93329105));
        brailleBlockPoints.add(new LatLng(37.52659162, 126.93330495));
        brailleBlockPoints.add(new LatLng(37.52659584, 126.93331306));
        brailleBlockPoints.add(new LatLng(37.52659883, 126.9333193));
        brailleBlockPoints.add(new LatLng(37.52660587, 126.93333385));
        brailleBlockPoints.add(new LatLng(37.52661219, 126.93334621));
        brailleBlockPoints.add(new LatLng(37.52661706, 126.93335491));
        brailleBlockPoints.add(new LatLng(37.5266216, 126.93336442));
        brailleBlockPoints.add(new LatLng(37.52662738, 126.93337791));
        brailleBlockPoints.add(new LatLng(37.52663246, 126.93338756));
        brailleBlockPoints.add(new LatLng(37.52664071, 126.9334082));
        brailleBlockPoints.add(new LatLng(37.52664808, 126.9334207));
        brailleBlockPoints.add(new LatLng(37.52665732, 126.93343753));
        brailleBlockPoints.add(new LatLng(37.52666805, 126.93346223));
        brailleBlockPoints.add(new LatLng(37.52667444, 126.93347819));
        brailleBlockPoints.add(new LatLng(37.52668385, 126.9334977));
        brailleBlockPoints.add(new LatLng(37.52669147, 126.93351433));
        brailleBlockPoints.add(new LatLng(37.52669706, 126.93352457));
        brailleBlockPoints.add(new LatLng(37.52670526, 126.93354217));
        brailleBlockPoints.add(new LatLng(37.52671311, 126.93355608));
        brailleBlockPoints.add(new LatLng(37.52672318, 126.93357898));
        brailleBlockPoints.add(new LatLng(37.52673202, 126.93359354));
        brailleBlockPoints.add(new LatLng(37.52673899, 126.93361357));
        brailleBlockPoints.add(new LatLng(37.52674483, 126.93362775));
        brailleBlockPoints.add(new LatLng(37.52674848, 126.93363972));
        brailleBlockPoints.add(new LatLng(37.52675343, 126.93365022));
        brailleBlockPoints.add(new LatLng(37.52675732, 126.93365891));
        brailleBlockPoints.add(new LatLng(37.5267613, 126.93366723));
        brailleBlockPoints.add(new LatLng(37.52675496, 126.93367693));
        brailleBlockPoints.add(new LatLng(37.52674645, 126.93368471));
        brailleBlockPoints.add(new LatLng(37.52673696, 126.93369044));
        brailleBlockPoints.add(new LatLng(37.52672674, 126.93369976));
        brailleBlockPoints.add(new LatLng(37.52671432, 126.9337105));
        brailleBlockPoints.add(new LatLng(37.5267073, 126.93372074));
        brailleBlockPoints.add(new LatLng(37.52669625, 126.93372851));
        brailleBlockPoints.add(new LatLng(37.52668719, 126.93373672));
        brailleBlockPoints.add(new LatLng(37.52667562, 126.9337462));
        brailleBlockPoints.add(new LatLng(37.52666133, 126.93375899));
        brailleBlockPoints.add(new LatLng(37.52664577, 126.93377332));
        brailleBlockPoints.add(new LatLng(37.52663131, 126.93378788));
        brailleBlockPoints.add(new LatLng(37.52661357, 126.93380054));
        brailleBlockPoints.add(new LatLng(37.52660505, 126.93380914));
        brailleBlockPoints.add(new LatLng(37.52659316, 126.93381855));
        brailleBlockPoints.add(new LatLng(37.52658212, 126.93382987));
        brailleBlockPoints.add(new LatLng(37.52656795, 126.9338459));
        brailleBlockPoints.add(new LatLng(37.5265484, 126.93386104));
        brailleBlockPoints.add(new LatLng(37.52653034, 126.93387893));
        brailleBlockPoints.add(new LatLng(37.52651572, 126.93389294));
        brailleBlockPoints.add(new LatLng(37.52650417, 126.93390358));
        brailleBlockPoints.add(new LatLng(37.52649148, 126.9339134));
        brailleBlockPoints.add(new LatLng(37.52647784, 126.93392635));
        brailleBlockPoints.add(new LatLng(37.52646353, 126.93394058));
        brailleBlockPoints.add(new LatLng(37.52645631, 126.9339459));
        brailleBlockPoints.add(new LatLng(37.52644755, 126.9339534));
        brailleBlockPoints.add(new LatLng(37.52643728, 126.93396197));
        brailleBlockPoints.add(new LatLng(37.52642649, 126.93397016));
        brailleBlockPoints.add(new LatLng(37.5264161, 126.93398008));
        brailleBlockPoints.add(new LatLng(37.5264039, 126.93398909));
        brailleBlockPoints.add(new LatLng(37.52638927, 126.93400235));
        brailleBlockPoints.add(new LatLng(37.52637867, 126.93401217));
        brailleBlockPoints.add(new LatLng(37.52636894, 126.93402125));
        brailleBlockPoints.add(new LatLng(37.52635822, 126.93402994));
        brailleBlockPoints.add(new LatLng(37.52634503, 126.93404018));
        brailleBlockPoints.add(new LatLng(37.52633151, 126.93405245));
        brailleBlockPoints.add(new LatLng(37.52632211, 126.93405982));
        brailleBlockPoints.add(new LatLng(37.52630026, 126.93407783));
        brailleBlockPoints.add(new LatLng(37.52628436, 126.93409353));
        brailleBlockPoints.add(new LatLng(37.52627376, 126.93410333));
        brailleBlockPoints.add(new LatLng(37.52625862, 126.93411274));
        brailleBlockPoints.add(new LatLng(37.52624489, 126.93412778));
        brailleBlockPoints.add(new LatLng(37.52623013, 126.93413843));
        brailleBlockPoints.add(new LatLng(37.52621588, 126.93414989));
        brailleBlockPoints.add(new LatLng(37.52620574, 126.93415848));
        brailleBlockPoints.add(new LatLng(37.52619428, 126.93416749));
        brailleBlockPoints.add(new LatLng(37.52618401, 126.93417527));
        brailleBlockPoints.add(new LatLng(37.52617452, 126.93418427));
        brailleBlockPoints.add(new LatLng(37.52615914, 126.93419549));
        brailleBlockPoints.add(new LatLng(37.5261468, 126.93420572));
        brailleBlockPoints.add(new LatLng(37.52613207, 126.93421878));
        brailleBlockPoints.add(new LatLng(37.52611673, 126.93423065));
        brailleBlockPoints.add(new LatLng(37.52610632, 126.93423803));
        brailleBlockPoints.add(new LatLng(37.52609515, 126.9342506));
        brailleBlockPoints.add(new LatLng(37.52608103, 126.93426074));
        brailleBlockPoints.add(new LatLng(37.52607252, 126.93426811));
        brailleBlockPoints.add(new LatLng(37.52606433, 126.9342768));
        brailleBlockPoints.add(new LatLng(37.52605678, 126.93428253));
        brailleBlockPoints.add(new LatLng(37.52604976, 126.93428662));
        brailleBlockPoints.add(new LatLng(37.52604846, 126.93427146));
        brailleBlockPoints.add(new LatLng(37.52604911, 126.93425826));
        brailleBlockPoints.add(new LatLng(37.52605073, 126.93424261));
        brailleBlockPoints.add(new LatLng(37.52605398, 126.93420853));
        brailleBlockPoints.add(new LatLng(37.5260556, 126.93418746));
        brailleBlockPoints.add(new LatLng(37.52605722, 126.93416949));
        brailleBlockPoints.add(new LatLng(37.52605755, 126.93415269));
        brailleBlockPoints.add(new LatLng(37.52605982, 126.93413359));
        brailleBlockPoints.add(new LatLng(37.52606144, 126.9341142));
        brailleBlockPoints.add(new LatLng(37.52606241, 126.93409608));
        brailleBlockPoints.add(new LatLng(37.52606403, 126.93407484));
        brailleBlockPoints.add(new LatLng(37.52606468, 126.93405779));
        brailleBlockPoints.add(new LatLng(37.52606323, 126.93403933));
        brailleBlockPoints.add(new LatLng(37.52606064, 126.93401914));
        brailleBlockPoints.add(new LatLng(37.52605544, 126.93400462));
        brailleBlockPoints.add(new LatLng(37.52604798, 126.9339956));
        brailleBlockPoints.add(new LatLng(37.52603913, 126.93398332));
        brailleBlockPoints.add(new LatLng(37.5260284, 126.9339682));
        brailleBlockPoints.add(new LatLng(37.52602061, 126.93395508));
        brailleBlockPoints.add(new LatLng(37.52601261, 126.93394733));
        brailleBlockPoints.add(new LatLng(37.52600677, 126.93393683));
        brailleBlockPoints.add(new LatLng(37.525999, 126.93392551));
        brailleBlockPoints.add(new LatLng(37.52600946, 126.93390914));
        brailleBlockPoints.add(new LatLng(37.52601928, 126.933903));
        brailleBlockPoints.add(new LatLng(37.52603071, 126.93389277));
        brailleBlockPoints.add(new LatLng(37.52604272, 126.93387876));
        brailleBlockPoints.add(new LatLng(37.52605148, 126.9338687));
        brailleBlockPoints.add(new LatLng(37.52606057, 126.93385452));
        brailleBlockPoints.add(new LatLng(37.52607129, 126.93383928));
        brailleBlockPoints.add(new LatLng(37.52607908, 126.93382895));
        brailleBlockPoints.add(new LatLng(37.52609076, 126.93381493));
        brailleBlockPoints.add(new LatLng(37.5261031, 126.93379854));
        brailleBlockPoints.add(new LatLng(37.52611284, 126.93378576));
        brailleBlockPoints.add(new LatLng(37.52612322, 126.93377051));
        brailleBlockPoints.add(new LatLng(37.52613978, 126.9337475));
        brailleBlockPoints.add(new LatLng(37.52615089, 126.93373153));
        brailleBlockPoints.add(new LatLng(37.52616518, 126.93371384));
        brailleBlockPoints.add(new LatLng(37.52617889, 126.93369548));
        brailleBlockPoints.add(new LatLng(37.52618936, 126.93368147));
        brailleBlockPoints.add(new LatLng(37.52620248, 126.93366134));
        brailleBlockPoints.add(new LatLng(37.52621955, 126.93364071));
        brailleBlockPoints.add(new LatLng(37.52623457, 126.93361574));
        brailleBlockPoints.add(new LatLng(37.52625047, 126.93359834));
        brailleBlockPoints.add(new LatLng(37.52626584, 126.93357506));
        brailleBlockPoints.add(new LatLng(37.52627676, 126.93356033));
        brailleBlockPoints.add(new LatLng(37.52628657, 126.93354877));
        brailleBlockPoints.add(new LatLng(37.52629502, 126.93353967));
        brailleBlockPoints.add(new LatLng(37.52630473, 126.93352385));
        brailleBlockPoints.add(new LatLng(37.52631253, 126.93351311));
        brailleBlockPoints.add(new LatLng(37.52632409, 126.93350174));
        brailleBlockPoints.add(new LatLng(37.52633383, 126.93348456));
        brailleBlockPoints.add(new LatLng(37.52634118, 126.93347678));
        brailleBlockPoints.add(new LatLng(37.52635449, 126.93346056));
        brailleBlockPoints.add(new LatLng(37.52636358, 126.93344436));
        brailleBlockPoints.add(new LatLng(37.52637686, 126.9334299));
        brailleBlockPoints.add(new LatLng(37.52638602, 126.93341639));
        brailleBlockPoints.add(new LatLng(37.52639836, 126.93339869));
        brailleBlockPoints.add(new LatLng(37.52641187, 126.93338601));
        brailleBlockPoints.add(new LatLng(37.52642518, 126.93337076));
        brailleBlockPoints.add(new LatLng(37.5264413, 126.93335398));
        brailleBlockPoints.add(new LatLng(37.52644844, 126.93334447));
        brailleBlockPoints.add(new LatLng(37.5264622, 126.9333286));
        brailleBlockPoints.add(new LatLng(37.52647129, 126.93331499));
        brailleBlockPoints.add(new LatLng(37.52648625, 126.93329682));
        brailleBlockPoints.add(new LatLng(37.52650132, 126.93327848));
        brailleBlockPoints.add(new LatLng(37.52651333, 126.93326365));
        brailleBlockPoints.add(new LatLng(37.52652644, 126.93324993));
        brailleBlockPoints.add(new LatLng(37.52653565, 126.93323726));
        brailleBlockPoints.add(new LatLng(37.52654286, 126.93322897));
        brailleBlockPoints.add(new LatLng(37.52654708, 126.93321971));
        brailleBlockPoints.add(new LatLng(37.52657753, 126.9332936));
        brailleBlockPoints.add(new LatLng(37.52657493, 126.93330802));
        brailleBlockPoints.add(new LatLng(37.52657233, 126.93332434));
        brailleBlockPoints.add(new LatLng(37.52657071, 126.93334152));
        brailleBlockPoints.add(new LatLng(37.52656844, 126.93335881));
        brailleBlockPoints.add(new LatLng(37.52656601, 126.93337799));
        brailleBlockPoints.add(new LatLng(37.52656504, 126.93339504));
        brailleBlockPoints.add(new LatLng(37.52656178, 126.93341423));
        brailleBlockPoints.add(new LatLng(37.52656114, 126.93343496));
        brailleBlockPoints.add(new LatLng(37.52656114, 126.93345563));
        brailleBlockPoints.add(new LatLng(37.52656016, 126.93347828));
        brailleBlockPoints.add(new LatLng(37.52655821, 126.93349778));
        brailleBlockPoints.add(new LatLng(37.52655821, 126.93351316));
        brailleBlockPoints.add(new LatLng(37.52655562, 126.93353244));
        brailleBlockPoints.add(new LatLng(37.52655172, 126.9335548));
        brailleBlockPoints.add(new LatLng(37.52654843, 126.9335758));
        brailleBlockPoints.add(new LatLng(37.52654453, 126.93359298));
        brailleBlockPoints.add(new LatLng(37.52654258, 126.93360904));
        brailleBlockPoints.add(new LatLng(37.52653804, 126.93362524));
        brailleBlockPoints.add(new LatLng(37.52653414, 126.93364487));
        brailleBlockPoints.add(new LatLng(37.52652734, 126.9336657));
        brailleBlockPoints.add(new LatLng(37.5265228, 126.93368503));
        brailleBlockPoints.add(new LatLng(37.5265189, 126.93370756));
        brailleBlockPoints.add(new LatLng(37.52651211, 126.93373337));
        brailleBlockPoints.add(new LatLng(37.52650548, 126.93375312));
        brailleBlockPoints.add(new LatLng(37.52649685, 126.93378257));
        brailleBlockPoints.add(new LatLng(37.52648744, 126.93380698));
        brailleBlockPoints.add(new LatLng(37.52647932, 126.93383164));
        brailleBlockPoints.add(new LatLng(37.52647138, 126.93385517));
        brailleBlockPoints.add(new LatLng(37.52646325, 126.93387697));
        brailleBlockPoints.add(new LatLng(37.52645936, 126.93389303));
        brailleBlockPoints.add(new LatLng(37.52645077, 126.93391738));
        brailleBlockPoints.add(new LatLng(37.5264446, 126.93393718));
        brailleBlockPoints.add(new LatLng(37.5264402, 126.93394575));
        brailleBlockPoints.add(new LatLng(37.52644471, 126.9339737));
        brailleBlockPoints.add(new LatLng(37.5264529, 126.93398281));
        brailleBlockPoints.add(new LatLng(37.52646166, 126.93399477));
        brailleBlockPoints.add(new LatLng(37.5264688, 126.93400428));
        brailleBlockPoints.add(new LatLng(37.52647919, 126.93401543));
        brailleBlockPoints.add(new LatLng(37.52648641, 126.9340228));
        brailleBlockPoints.add(new LatLng(37.5264933, 126.93403017));
        brailleBlockPoints.add(new LatLng(37.52649987, 126.93403753));
        brailleBlockPoints.add(new LatLng(37.52650968, 126.93405009));
        brailleBlockPoints.add(new LatLng(37.52651766, 126.93405926));
        brailleBlockPoints.add(new LatLng(37.52652739, 126.9340712));
        brailleBlockPoints.add(new LatLng(37.52654271, 126.93408593));
        brailleBlockPoints.add(new LatLng(37.52655668, 126.93410067));
        brailleBlockPoints.add(new LatLng(37.52656884, 126.93411598));
        brailleBlockPoints.add(new LatLng(37.52658255, 126.93413031));
        brailleBlockPoints.add(new LatLng(37.52659705, 126.93414675));
        brailleBlockPoints.add(new LatLng(37.52661219, 126.93416411));
        brailleBlockPoints.add(new LatLng(37.52663329, 126.93418713));
        brailleBlockPoints.add(new LatLng(37.52664992, 126.93420555));
        brailleBlockPoints.add(new LatLng(37.52666452, 126.93422096));
        brailleBlockPoints.add(new LatLng(37.52667693, 126.9342357));
        brailleBlockPoints.add(new LatLng(37.5266861, 126.93424634));
        brailleBlockPoints.add(new LatLng(37.52669429, 126.93425535));
        brailleBlockPoints.add(new LatLng(37.52670305, 126.9342672));
        brailleBlockPoints.add(new LatLng(37.52671092, 126.93427498));
        brailleBlockPoints.add(new LatLng(37.52671968, 126.93428408));
        brailleBlockPoints.add(new LatLng(37.5267334, 126.93429882));
        brailleBlockPoints.add(new LatLng(37.52673931, 126.93430537));
        brailleBlockPoints.add(new LatLng(37.52674848, 126.93431692));
        brailleBlockPoints.add(new LatLng(37.52675894, 126.93432798));
        brailleBlockPoints.add(new LatLng(37.5267674, 126.93433903));
        brailleBlockPoints.add(new LatLng(37.52677527, 126.93434721));
        brailleBlockPoints.add(new LatLng(37.52678371, 126.9343552));
        brailleBlockPoints.add(new LatLng(37.52678859, 126.93436011));
        brailleBlockPoints.add(new LatLng(37.52679413, 126.93436972));
        brailleBlockPoints.add(new LatLng(37.5267994, 126.93437381));
    }

    // 지도에 점자블록 좌표 추가하는 함수 (별도의 함수 호출 없이 직접 추가)
    public void addBrailleBlockOnMap(@NonNull NaverMap naverMap) {
        for (LatLng point : brailleBlockPoints) {
            CircleOverlay circle = new CircleOverlay();
            circle.setCenter(point);
            circle.setRadius(1.0); // 반경 1m
            circle.setColor(0x40FFA500); // 주황색 반투명
            circle.setOutlineColor(0xFFFFA500); // 주황색 테두리
            circle.setOutlineWidth(3); // 테두리 두께
            circle.setMap(naverMap); // 지도에 원 추가
        }

        // 사용자 위치 변화 리스너 추가
        naverMap.addOnLocationChangeListener(location -> {
            LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());
            checkUserProximity(userPosition);  // 사용자와 좌표 간의 거리 계산 및 알림
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

    // 사용자와 세 좌표 간의 최소 거리를 계산하고 알림을 보내는 함수
    private void checkUserProximity(LatLng userPosition) {
        double minDistance = Double.MAX_VALUE;

        // 사용자와 각 점자블록 좌표 간의 거리를 계산
        for (LatLng point : brailleBlockPoints) {
            double distance = distanceBetween(userPosition, point);
            if (distance < minDistance) {
                minDistance = distance; // 최소 거리 업데이트
            }
        }

        // 최소 거리가 2m 이내일 때 Toast 신호가 계속 발생하도록 처리
        if (minDistance <= 2.0) {
            if (!isUserNearBrailleBlock) { // 처음 2m 이내로 들어올 때
                isUserNearBrailleBlock = true; // 상태 업데이트
            }
            Toast.makeText(context, "점자블록 근처입니다.", Toast.LENGTH_SHORT).show(); // 계속 신호 발생
        } else {
            if (isUserNearBrailleBlock) { // 처음 2m 이상으로 벗어났을 때
                isUserNearBrailleBlock = false; // 상태 업데이트
            }
            // 2m 이상일 때도 신호를 계속 발생
            Toast.makeText(context, "점자블록에서 2m 이상 떨어졌습니다.", Toast.LENGTH_SHORT).show(); // 계속 신호 발생
        }
    }
}
