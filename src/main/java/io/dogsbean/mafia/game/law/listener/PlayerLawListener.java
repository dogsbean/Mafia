package io.dogsbean.mafia.game.law.listener;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.NPCAction;
import io.dogsbean.mafia.game.law.laws.ArsonLaw;
import io.dogsbean.mafia.game.law.laws.AssaultLaw;
import io.dogsbean.mafia.game.law.laws.MurderLaw;
import io.dogsbean.mafia.npc.NPC;
import io.dogsbean.mafia.npc.Personality;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

public class PlayerLawListener implements Listener {

    @EventHandler
    public void onFire(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() == Material.FIRE || block.getType() == Material.BURNING_FURNACE) {
            if (isArson(player)) {
                NPC npc = Main.getInstance().getNpcManager().getNearestVillagerWithinRange(player.getLocation(), 10);
                if (npc != null) {
                    npc.onPlayerViolation(player, new ArsonLaw());
                }
            }
        }
    }

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
            Integer trustLevel = npc.getTrustLevel().get(player);
            if (trustLevel != null && trustLevel > 0) {
                npc.updateTrust(player, -20);
            }

            npc.getVillager().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, 3));
            npc.interactAttack(player);
        }

        for (NPC nearbyNPC : Main.getInstance().getNpcManager().getNPCS()) {
            if (nearbyNPC != npc && nearbyNPC.getVillager().getLocation().distance(npc.getVillager().getLocation()) <= 10) {
                for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
                    Integer nearbyTrustLevel = nearbyNPC.getTrustLevel().get(nearbyPlayer);
                    if (nearbyTrustLevel != null) {
                        nearbyNPC.updateTrust(nearbyPlayer, -5);
                        nearbyNPC.getVillager().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, 3));
                        npc.onPlayerViolation(player, new AssaultLaw());
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
                        npc.onPlayerViolation(player, new MurderLaw());
                    }
                }
                npc.updatePersonality(20);
            }
        }
    }

    private boolean isArson(Player player) {
        return player.getInventory().getItemInMainHand().getType().equals(Material.FLINT_AND_STEEL);
    }
}
