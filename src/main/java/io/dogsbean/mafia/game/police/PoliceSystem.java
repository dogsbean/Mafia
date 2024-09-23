package io.dogsbean.mafia.game.police;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.Crime;
import io.dogsbean.mafia.game.law.Criminal;
import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.npc.NPC;
import io.dogsbean.mafia.util.PlayerTitle;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class PoliceSystem {
    private Map<Player, NPC> reportedNPCs = new HashMap<>();
    @Getter private Map<UUID, Law> reportedPlayer = new HashMap<>();
    @Getter private List<UUID> arrested = new ArrayList<>();
    @Getter private Map<UUID, IronGolem> polices = new HashMap<>();
    private BukkitTask policeSpawnTask;
    private BukkitTask policeRemoveTask;

    public void reportPlayer(NPC npc, Player player, Law law) {
        if (reportedPlayer.get(player.getUniqueId()) != null) {
            return;
        }

        PlayerTitle.sendTitleToPlayer(player, ChatColor.RED + "범죄 신고", ChatColor.YELLOW + "시민이 당신을 신고했습니다.");

        reportedNPCs.put(player, npc);
        reportedPlayer.put(player.getUniqueId(), law);
        player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "경찰이 곧 도착합니다. 빨리 피하세요!");
        policeSpawnTask = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            spawnPolice(player);
            if (isPlayerHiding(npc, player)) {
                IronGolem ironGolem = polices.get(player.getUniqueId());
                ironGolem.setTarget(null);
            }
        }, 200L);
    }

    private boolean isPlayerHiding(NPC npc, Player player) {
        return npc.getVillager().getLocation().distance(player.getLocation()) > 30 && Main.getInstance().getNpcManager().canNPCSeePlayer(npc.getVillager(), player);
    }

    private void spawnPolice(Player player) {
        if (polices.containsKey(player.getUniqueId())) {
            return;
        }

        NPC npc = reportedNPCs.get(player);
        if (npc == null || npc.getVillager().isDead()) {
            player.sendMessage(ChatColor.RED + "신고된 NPC가 유효하지 않습니다.");
            reportedNPCs.remove(player);
            reportedPlayer.remove(player.getUniqueId());
            return;
        }

        IronGolem police = (IronGolem) player.getWorld().spawnEntity(npc.getVillager().getLocation(), EntityType.IRON_GOLEM);
        police.setCustomName("경찰");
        police.setCustomNameVisible(true);
        polices.put(player.getUniqueId(), police);

        if (npc.getVillager().isDead()) {
            Crime murder = new Crime("살인", "사람을 죽이는 범죄", 90);
            Criminal.commitCrime(player.getName(), murder);
        }
        player.sendMessage("경찰이 출동했습니다!");

        police.setTarget(player);

        policeRemoveTask = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            if (!arrested.contains(player.getUniqueId()) && police.getLocation().distance(player.getLocation()) < 30) {
                removePolices(player);
                player.sendMessage(ChatColor.GREEN + "경찰이 사라졌습니다. 안전합니다.");
            }
        }, 600L);
    }

    public void arrestPlayer(Player player) {
        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "경찰에 의해 체포되었습니다!");
        arrested.add(player.getUniqueId());
    }

    public void removePolices(Player player) {
        Bukkit.broadcastMessage("asadf");
        if (polices.containsKey(player.getUniqueId())) {
            IronGolem police = polices.get(player.getUniqueId());
            if (police != null) {
                police.setTarget(null);
                police.remove();
                polices.remove(player.getUniqueId());
                reportedPlayer.remove(player.getUniqueId());
                reportedNPCs.remove(player);
                Bukkit.getLogger().info("플레이어 " + player.getName() + "의 경찰이 제거되었습니다.");
            }
        } else {
            player.sendMessage("당신의 경찰이 없습니다.");
        }
    }

    public IronGolem getNearByPolice(Location location) {
        for (Map.Entry<UUID, IronGolem> entry : polices.entrySet()) {
            IronGolem police = entry.getValue();
            if (police != null && police.isValid()) {
                if (police.getLocation().distance(location) <= 30) {
                    return police;
                }
            }
        }
        return null;
    }

    public boolean canPoliceSeePlayer(IronGolem ironGolem, Player player1) {
        Location playerEyeLocation = player1.getEyeLocation();
        Location villagerEyeLocation = ironGolem.getEyeLocation();
        Vector toPlayer = playerEyeLocation.toVector().subtract(villagerEyeLocation.toVector()).normalize();
        Vector villagerDirection = villagerEyeLocation.getDirection();

        double dot = toPlayer.dot(villagerDirection);
        if (dot > 0.99D) {
            player1.sendMessage("Police detected you");
            return ironGolem.hasLineOfSight(player1);
        }

        return false;
    }

    public void clear(Player player) {
        reportedNPCs.clear();
        reportedPlayer.clear();
        removePolices(player);
        arrested.clear();

        if (policeSpawnTask != null) {
            policeSpawnTask.cancel();
        }

        if (policeRemoveTask != null) {
            policeRemoveTask.cancel();
        }
    }
}
