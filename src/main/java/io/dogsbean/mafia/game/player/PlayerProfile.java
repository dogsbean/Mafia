package io.dogsbean.mafia.game.player;

import io.dogsbean.mafia.game.law.Criminal;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerProfile {
    private static final Map<Player, PlayerProfile> profiles = new HashMap<>();

    private Player player;
    @Getter private int money;
    @Getter private int arrestedCount;

    private PlayerProfile(Player player) {
        this.player = player;
        initializeProfile(); // 프로필 초기화
    }

    public static PlayerProfile getProfile(Player player) {
        return profiles.computeIfAbsent(player, PlayerProfile::new);
    }

    public void initializeProfile() {
        this.money = 10000;
        this.arrestedCount = 0;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void removeMoney(int amount) {
        this.money -= amount;
    }
}
