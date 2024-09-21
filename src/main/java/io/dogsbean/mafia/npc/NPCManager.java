package io.dogsbean.mafia.npc;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import java.util.Random;
import org.bukkit.entity.Villager;

import java.util.*;

public class NPCManager {
    private List<NPC> npcVillagers = new ArrayList<>();
    private Random random = new Random();

    private final String[] villagerPrefix = {
            "한가한",
            "바쁜",
            "호기심 많은",
            "겁에 질린",
            "귀찮은"
    };

    private final Villager.Profession[] professions = {
            Villager.Profession.FARMER,
            Villager.Profession.LIBRARIAN,
            Villager.Profession.PRIEST,
            Villager.Profession.BLACKSMITH
    };

    public void loadVillagers(World world) {
        Location npcLocation = new Location(world, 100, 65, 100); // TODO: 아레나 pvp 플러그인 처럼 울타리 위에 머리 설치한 곳 무작위 좌표에 스폰
        Villager villager = (Villager) world.spawnEntity(npcLocation, EntityType.VILLAGER);
        villager.setCustomName(getUniqueVillagerName());
        villager.setCustomNameVisible(true);
        villager.setProfession(getRandomProfession());

        npcVillagers.add(new NPC(villager, Personality.FRIENDLY));
    }

    private String getUniqueVillagerName() {
        String name;
        int attempt = 0;

        // 중복되지 않는 이름을 찾기 위해 시도
        do {
            name = getRandomVillagerName();
            attempt++;
        } while (isNameUsed(name) && attempt < 100); // 중복된 이름이 있을 경우 다시 생성 (최대 100회 시도)

        return name;
    }

    private String getRandomVillagerName() {
        String title = villagerPrefix[random.nextInt(villagerPrefix.length)];
        int randomNumber = random.nextInt(10) + 1;
        return title + " 시민 " + randomNumber;
    }

    private Villager.Profession getRandomProfession() {
        return professions[random.nextInt(professions.length)];
    }

    private boolean isNameUsed(String name) {
        return npcVillagers.stream()
                .anyMatch(npc -> npc.getVillager().getCustomName().equals(name));
    }

    public NPC getNearestVillager(Location location) {
        // 특정 위치에 가장 가까운 Villager NPC 반환
        return npcVillagers.stream()
                .min(Comparator.comparingDouble(npc -> npc.getVillager().getLocation().distance(location)))
                .orElse(null);
    }
}
