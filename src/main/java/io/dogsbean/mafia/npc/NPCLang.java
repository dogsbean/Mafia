package io.dogsbean.mafia.npc;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class NPCLang {
    private static final Map<Personality, Map<Integer, String>> messages = new HashMap<>();
    private static final Map<Integer, String> healthMessages = new HashMap<>();

    static {
        for (Personality personality : Personality.values()) {
            messages.put(personality, new HashMap<>());
        }

        messages.get(Personality.FRIENDLY).put(0, "저는 당신을 믿지 않아요.");
        messages.get(Personality.FRIENDLY).put(25, "그냥 좀 더 지켜볼게요.");
        messages.get(Personality.FRIENDLY).put(50, "조금씩 믿게 되었어요.");
        messages.get(Personality.FRIENDLY).put(75, "당신을 믿어요! 무엇을 도와줄까요?");
        messages.get(Personality.FRIENDLY).put(100, "당신은 정말 좋은 친구예요!");

        messages.get(Personality.NEUTRAL).put(0, "뭐, 그렇게 나쁘지 않아요.");
        messages.get(Personality.NEUTRAL).put(50, "보통이네요.");
        messages.get(Personality.NEUTRAL).put(100, "우리는 좋은 관계를 유지할 수 있어요.");

        messages.get(Personality.HOSTILE).put(0, "당신에게는 말하고 싶지 않아요.");
        messages.get(Personality.HOSTILE).put(25, "너무 신경 쓰지 말아주세요.");
        messages.get(Personality.HOSTILE).put(50, "전 당신을 믿지 않아요!");
        messages.get(Personality.HOSTILE).put(75, "가까이 오지 마세요!");
        messages.get(Personality.HOSTILE).put(100, "당신과는 대화할 생각이 없어요.");

        healthMessages.put(25, "살려주세요!");
        healthMessages.put(50, "제발 그만 때려주세요!");
        healthMessages.put(75, "왜 그러시는 건가요?");
        healthMessages.put(100, "공격하지 마세요!");
    }

    public static String getMessage(NPC npc, Player player) {
        int trustLevel = npc.getTrustLevel().getOrDefault(player, 0);
        Personality personality = npc.getPersonality();

        return messages.get(personality).entrySet().stream()
                .filter(entry -> trustLevel <= entry.getKey())
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse("이해할 수 없는 반응입니다.");
    }

    public static String getHealthBasedMessage(NPC npc) {
        double health = npc.getVillager().getHealth();
        double maxHealth = npc.getVillager().getMaxHealth();
        double healthPercentage = (health / maxHealth) * 100;

        return healthMessages.entrySet().stream()
                .filter(entry -> healthPercentage <= entry.getKey())
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse("이해할 수 없는 반응입니다.");
    }
}