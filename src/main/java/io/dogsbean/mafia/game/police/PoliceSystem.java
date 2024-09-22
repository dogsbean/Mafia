package io.dogsbean.mafia.game.police;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.npc.NPC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;

import java.util.*;

public class PoliceSystem {
    private List<NPC> reportedNPCs = new ArrayList<>();
    @Getter private List<UUID> reportedPlayer = new ArrayList<>();
    @Getter private List<UUID> arrested = new ArrayList<>();
    @Getter private Map<UUID, IronGolem> polices = new HashMap<>();

    public void reportPlayer(NPC npc, Player player) {
        if (reportedPlayer.contains(player.getUniqueId())) {
            return;
        }

        reportedNPCs.add(npc);
        reportedPlayer.add(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "경찰이 곧 도착합니다. 빨리 피하세요!");
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            spawnPolice(player);
            if (isPlayerHiding(npc, player)) {
                IronGolem ironGolem = polices.get(player.getUniqueId());
                ironGolem.setTarget(null);
            }
        }, 200L);
    }

    private boolean isPlayerHiding(NPC npc, Player player) {
        return npc.getVillager().getLocation().distance(player.getLocation()) > 30;
    }

    private void spawnPolice(Player player) {
        if (polices.containsKey(player.getUniqueId())) {
            return;
        }

        IronGolem police = (IronGolem) player.getWorld().spawnEntity(reportedNPCs.get(0).getVillager().getLocation(), EntityType.IRON_GOLEM);
        police.setCustomName("경찰");
        police.setCustomNameVisible(true);
        polices.put(player.getUniqueId(), police);

        player.sendMessage("경찰이 출동했습니다! 도망치지 마세요!");

        police.setTarget(player);
    }

    public void arrestPlayer(Player player) {
        player.sendMessage("경찰에 의해 체포되었습니다!");
        arrested.add(player.getUniqueId());
    }

    public void removePolices(Player player) {
        if (polices.containsKey(player.getUniqueId())) {
            IronGolem police = polices.get(player.getUniqueId());
            if (police != null) {
                police.setTarget(null);
                police.remove();
                polices.remove(player.getUniqueId());
                Bukkit.getLogger().info("플레이어 " + player.getName() + "의 경찰이 제거되었습니다.");
            }
        } else {
            player.sendMessage("당신의 경찰이 없습니다.");
        }
    }

    public void clear(Player player) {
        reportedNPCs.clear();
        reportedPlayer.clear();
        removePolices(player);
        arrested.clear();
    }
}
