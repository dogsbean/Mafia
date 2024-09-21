package io.dogsbean.mafia.role.manager.cctv;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class CCTV {
    private Location location;
    private int range; // CCTV 시야 범위

    public CCTV(Location location) {
        this.location = location;
        this.range = 10; // 기본 시야 범위
    }

    public boolean isInRange(Location targetLocation) {
        return location.distance(targetLocation) <= range;
    }

    public List<Player> getPlayersInView() {
        // CCTV 시야 내에 있는 플레이어 반환
    }
}
