package io.dogsbean.mafia.role.manager.cctv;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CCTVManager {
    private List<CCTV> cctvs = new ArrayList<>();

    public void setupCCTV(Location location) {
        cctvs.add(new CCTV(location));
    }

    public void monitor(Player player) {
        // 플레이어가 CCTV를 모니터링 할 때 보여줄 정보
    }
}
