package io.dogsbean.mafia.npc;

import io.dogsbean.mafia.npc.event.PersonalityChangeEvent;
import io.dogsbean.mafia.npc.event.TrustLevelChangeEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.Map;

public class NPC {
    @Getter private final Villager villager;
    @Getter private Personality personality;
    @Getter private Map<Player, Integer> trustLevel;

    public NPC(Villager villager, Personality personality) {
        this.villager = villager;
        this.personality = personality;
        this.trustLevel = new HashMap<>();
    }

    public void initializeTrust(Player player, int initialTrust) {
        trustLevel.put(player, initialTrust); // 플레이어의 신뢰도 초기화
    }

    public Villager getVillager() {
        return villager;
    }

    public void interact(Player player) {
        String responseMessage = NPCLang.getMessage(this, player);
        player.sendMessage(villager.getCustomName() + ": " + responseMessage);
    }

    public void interactAttack(Player player) {
        String responseMessage = NPCLang.getHealthBasedMessage(this);
        player.sendMessage(villager.getCustomName() + ": " + responseMessage);
    }

    public String provideInformation(Player player) {
        int trust = trustLevel.getOrDefault(player, 0);
        if (trust > 50) {
            return "신뢰할만한 정보를 제공합니다.";
        } else {
            return "정보를 제공하지 않거나 거짓 정보를 제공합니다.";
        }
    }

    public void decreaseTrust(Player player, int amount) {
        int currentTrust = trustLevel.getOrDefault(player, 50);
        trustLevel.put(player, Math.max(0, currentTrust - amount));
    }

    public void updateTrust(Player player, int amount) {
        int currentTrust = trustLevel.getOrDefault(player, 0);
        int newTrust = currentTrust + amount;

        newTrust = Math.max(newTrust, 0);

        if (newTrust != currentTrust) {
            trustLevel.put(player, newTrust);
            Bukkit.getPluginManager().callEvent(new TrustLevelChangeEvent(player, this, currentTrust, newTrust));
        }
    }

    public void updatePersonality(int amount) {
        Personality currentPersonality = personality;
        Personality newPersonality = getPersonalityByLevel(personality.getPersonalityLevel() - amount);

        if (currentPersonality != newPersonality) {
            personality = newPersonality;
            Bukkit.getPluginManager().callEvent(new PersonalityChangeEvent(this, currentPersonality, newPersonality));
        }
    }

    private Personality getPersonalityByLevel(int level) {
        // 예시: level이 범위를 초과할 경우 null 대신 기본값 반환
        if (level < 0 || level >= Personality.values().length) {
            return Personality.FRIENDLY; // 기본 Personality 반환
        }
        return Personality.values()[level]; // 범위 내일 경우 해당 Personality 반환
    }
}
