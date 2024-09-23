package io.dogsbean.mafia.npc;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class NPCManager {
    @Getter private List<NPC> npcVillagers = new ArrayList<>();
    private Random random = new Random();

    private final String[] villagerPrefix = {
            "한가한",
            "바쁜",
            "호기심 많은",
            "귀찮은"
    };

    private final Villager.Profession[] professions = {
            Villager.Profession.FARMER,
            Villager.Profession.LIBRARIAN,
            Villager.Profession.PRIEST,
            Villager.Profession.BLACKSMITH
    };

    public void loadVillagers(World world, Player player) {
        if (world == null || player == null) {
            Bukkit.getLogger().info("World or player is null!");
            return;
        }

        for (int i = 0; i < 4; i++) {
            int offsetX = random.nextInt(61) - 30;
            int offsetZ = random.nextInt(61) - 30;
            Location npcLocation = player.getLocation().add(offsetX, 0, offsetZ);
            npcLocation.setY(world.getHighestBlockYAt(npcLocation));

            Villager villager = (Villager) world.spawnEntity(npcLocation, EntityType.VILLAGER);
            villager.setCustomName(getUniqueVillagerName());
            villager.setCustomNameVisible(true);
            villager.setProfession(getRandomProfession());

            Personality personality = random.nextInt(3) == 0 ? Personality.HOSTILE : Personality.FRIENDLY;
            NPC npc = new NPC(villager, personality);
            npc.initializeTrust(player, 50);
            npcVillagers.add(npc);
            Bukkit.getLogger().info("NPC added: " + npc.getVillager().getUniqueId());
        }

        Bukkit.getLogger().info("Total NPC count after adding: " + npcVillagers.size());
    }

    public void removeAllVillagers() {
        for (NPC npc : npcVillagers) {
            npc.getVillager().remove();
        }
        npcVillagers.clear();
        Bukkit.getLogger().info("All NPCs removed.");
    }

    private String getUniqueVillagerName() {
        String name;
        int attempt = 0;

        do {
            name = getRandomVillagerName();
            attempt++;
        } while (isNameUsed(name) && attempt < 100);

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

    public List<NPC> getNearestVillagers(Villager villager, Player player, int limit) {
        return npcVillagers.stream()
                .filter(npc -> npc.getVillager() != villager)
                .filter(npc -> canNPCSeePlayer(npc.getVillager(), player)) // 시야 조건 추가
                .sorted(Comparator.comparingDouble(npc -> npc.getVillager().getLocation().distance(villager.getLocation())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<NPC> getNearestVillagersWithinRange(Villager villager, Player player, double range) {
        return npcVillagers.stream()
                .filter(npc -> npc.getVillager() != villager)
                .filter(npc -> canNPCSeePlayer(npc.getVillager(), player)) // 시야 조건 추가
                .filter(npc -> npc.getVillager().getLocation().distance(villager.getLocation()) <= range)
                .sorted(Comparator.comparingDouble(npc -> npc.getVillager().getLocation().distance(villager.getLocation())))
                .collect(Collectors.toList());
    }

    public List<NPC> getNearestVillagersWithinRange(Location location, Player player, double range) {
        return npcVillagers.stream()
                .filter(npc -> canNPCSeePlayer(npc.getVillager(), player)) // 시야 조건 추가
                .filter(npc -> npc.getVillager().getLocation().distance(location) <= range)
                .sorted(Comparator.comparingDouble(npc -> npc.getVillager().getLocation().distance(location)))
                .collect(Collectors.toList());
    }

    public NPC getNearestVillagerWithinRange(Location location, Player player, double range) {
        return npcVillagers.stream()
                .filter(npc -> canNPCSeePlayer(npc.getVillager(), player)) // 시야 조건 추가
                .filter(npc -> npc.getVillager().getLocation().distance(location) <= range)
                .min(Comparator.comparingDouble(npc -> npc.getVillager().getLocation().distance(location)))
                .orElse(null);
    }

    public boolean canNPCSeePlayer(Villager villager, Player player1) {
        Location eye = villager.getEyeLocation();
        Vector toEntity = player1.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99D;
    }

    public NPC getNPC(Villager villager) {
        return npcVillagers.stream()
                .filter(npc -> npc.getVillager() == villager)
                .findFirst()
                .orElse(null);
    }

    public NPC getNPC(UUID uuid) {
        return npcVillagers.stream()
                .filter(npc -> npc.getVillager().getUniqueId().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public List<NPC> getNPCS() {
        return npcVillagers;
    }
}
