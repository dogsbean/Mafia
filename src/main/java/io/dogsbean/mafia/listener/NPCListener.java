package io.dogsbean.mafia.listener;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.npc.NPC;
import io.dogsbean.mafia.npc.Personality;
import io.dogsbean.mafia.npc.event.PersonalityChangeEvent;
import io.dogsbean.mafia.npc.event.TrustLevelChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NPCListener implements Listener {

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() == null || event.getEntity() == null) {
            return;
        }

        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.VILLAGER) {
            return;
        }

        Player player = (Player) event.getDamager();
        if (!Main.getInstance().getGameManager().isGameInProgress()) {
            player.sendMessage("Game is not in progress.");
            return;
        }

        if (!Main.getInstance().getGameManager().getPlayers().contains(player)) {
            player.sendMessage("You are not in the game.");
            return;
        }

        NPC npc = Main.getInstance().getNpcManager().getNPC(event.getEntity().getUniqueId());
        if (npc == null || npc.getVillager() != event.getEntity()) {
            player.sendMessage("이 NPC는 존재하지 않습니다.");
            return;
        }

        // 호전적인 NPC의 반응 처리
        double healthPercentage = (npc.getVillager().getHealth() / npc.getVillager().getMaxHealth()) * 100;
        if (npc.getPersonality() == Personality.HOSTILE) {
            if (healthPercentage > 50) {
                player.sendMessage("뭐야? 왜 때려?");
            } else {
                npc.interactAttack(player);
            }
            npc.getVillager().setTarget(player);
            player.damage(1.0, npc.getVillager());
        } else {
            // 기존 로직: 신뢰 수준 감소 및 메시지 처리
            Integer trustLevel = npc.getTrustLevel().get(player);
            if (trustLevel != null && trustLevel > 0) {
                npc.updateTrust(player, -20);
            }

            npc.getVillager().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, 3));
            npc.interactAttack(player);
        }

        // 주변 NPC의 반응 처리
        for (NPC nearbyNPC : Main.getInstance().getNpcManager().getNPCS()) {
            if (nearbyNPC != npc && nearbyNPC.getVillager().getLocation().distance(npc.getVillager().getLocation()) <= 10) {
                for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
                    Integer nearbyTrustLevel = nearbyNPC.getTrustLevel().get(nearbyPlayer);
                    if (nearbyTrustLevel != null) {
                        nearbyNPC.updateTrust(nearbyPlayer, -5);
                        nearbyNPC.getVillager().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, 3));
                    }
                }
                nearbyNPC.updatePersonality(5);
            }
        }
    }

    @EventHandler
    public void onNPCDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() != EntityType.VILLAGER) {
            return;
        }

        Villager deadVillager = (Villager) event.getEntity();
        NPC deadNPC = Main.getInstance().getNpcManager().getNPC(deadVillager.getUniqueId());

        if (deadNPC == null) {
            return;
        }

        for (NPC npc : Main.getInstance().getNpcManager().getNearestVillagersWithinRange(deadVillager, 10)) {
            if (npc != deadNPC) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Integer trustLevel = npc.getTrustLevel().get(player);
                    if (trustLevel != null) {
                        npc.updateTrust(player, -20);
                    }
                }
                npc.updatePersonality(20);
            }
        }
    }

    @EventHandler
    public void trustLevelChange(TrustLevelChangeEvent event) {
        if (event.getPlayer() == null) {
            if (!event.getNPC().getPersonality().equals(Personality.HOSTILE)) {
                event.getNPC().updatePersonality(20);
            }
            return;
        }

        if (!event.getNPC().getPersonality().equals(Personality.HOSTILE)) {
            event.getNPC().updatePersonality(20);
        }
    }
}
