package io.dogsbean.mafia.npc;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.Map;

public class NPC {
    private Villager villager;
    private Personality personality;
    private Map<Player, Integer> trustLevel;

    public NPC(Villager villager, Personality personality) {
        this.villager = villager;
        this.personality = personality;
        this.trustLevel = new HashMap<>();
    }

    public Villager getVillager() {
        return villager;
    }

    public void interact(Player player) {
        String prefix = villager.getCustomName().split(" ")[0]; // 이름에서 접두사 추출

        if (personality == Personality.FRIENDLY) {
            switch (prefix) {
                case "한가한":
                    player.sendMessage(villager.getCustomName() + ": " + "무슨 일이에요?");
                    break;
                case "바쁜":
                    player.sendMessage(villager.getCustomName() + ": " + "제가 좀 바빠서..");
                    break;
                case "호기심 많은":
                    player.sendMessage(villager.getCustomName() + ": " + "무슨 일인가요?");
                    break;
                case "겁에 질린":
                    player.sendMessage(villager.getCustomName() + ": " + "지금 얘기할 기분이 아니에요. 나중에 다시 애기해요.");
                    break;
                case "귀찮은":
                    player.sendMessage(villager.getCustomName() + ": " + "귀찮은걸요.");
                    break;
                default:
                    player.sendMessage(villager.getCustomName() + ": " + "안녕하세요.");
                    break;
            }
        } else if (personality == Personality.HOSTILE) {
            player.sendMessage(villager.getCustomName() + ": " + "가까이 오지 마세요.");
        }
    }

    public String provideInformation(Player player) {
        int trust = trustLevel.getOrDefault(player, 0);
        if (trust > 50) {
            return "신뢰할만한 정보를 제공합니다.";
        } else {
            return "정보를 제공하지 않거나 거짓 정보를 제공합니다.";
        }
    }
}
