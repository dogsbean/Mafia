package io.dogsbean.mafia.game.law.listener;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.laws.ArsonLaw;
import io.dogsbean.mafia.game.law.laws.AssaultLaw;
import io.dogsbean.mafia.game.law.laws.MurderLaw;
import io.dogsbean.mafia.game.quest.Quest;
import io.dogsbean.mafia.game.quest.QuestEndReason;
import io.dogsbean.mafia.game.quest.QuestType;
import io.dogsbean.mafia.npc.NPC;
import io.dogsbean.mafia.npc.Personality;
import io.dogsbean.mafia.util.PlayerTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerLawListener implements Listener {
    @EventHandler
    public void onFire(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() == Material.FIRE || block.getType() == Material.BURNING_FURNACE) {
            if (isArson(player)) {
                NPC npc = Main.getInstance().getNpcManager().getNearestVillagerWithinRange(player.getLocation(), player, 10);
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
                Main.getInstance().getLlamaAPI().sendLlamaMessage(npc.getPersonality(), player, "(상황: 당신의 앞에 보이는 사람이 한국인 말을 사용하는 당신을 폭행합니다. 당신은 그를 경계합니다. 당신은 호전적입니다. 당신은 생명의 위협을 느끼지 못합니다. 최대한 상황에 맞게 반응해보세요. 예: '왜 그러는 거야?' 또는 '그만해!')");
            } else {
                Main.getInstance().getLlamaAPI().sendLlamaMessage(npc.getPersonality(), player, "(상황: 당신의 앞에 보이는 사람이 한국인 말을 사용하는 당신을 폭행합니다. 당신은 그를 경계합니다. 당신은 호전적입니다. 당신은 생명의 위협을 느낍니다. 최대한 상황에 맞게 반응해보세요. 예: '죽을 것 같아요 제발 살려주세요' 또는 '살려주세요!')");
            }
            npc.getVillager().setTarget(player);
            player.damage(1.0, npc.getVillager());
        } else {
            Integer trustLevel = npc.getTrustLevel().get(player);
            if (trustLevel != null && trustLevel > 0) {
                npc.updateTrust(player, -20);
            }

            npc.getVillager().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, 3));
            if (healthPercentage > 50) {
                Main.getInstance().getLlamaAPI().sendLlamaMessage(npc.getPersonality(), player, "(상황: 당신의 앞에 보이는 사람이 한국인 말을 사용하는 당신을 폭행합니다. 당신은 그와 친합니다. 당신은 아직 생명의 위협을 느끼지 못합니다. 최대한 상황에 맞게 반응해보세요.)");
            } else {
                Main.getInstance().getLlamaAPI().sendLlamaMessage(npc.getPersonality(), player, "(상황: 당신의 앞에 보이는 사람이 한국인 말을 사용하는 당신을 폭행합니다. 당신은 그와 친합니다. 당신은 생명의 위협을 느낍니다. 최대한 상황에 맞게 반응해보세요.)");
            }
        }

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

        if (Main.getInstance().getPoliceSystem().getNearByPolice(player.getLocation()) != null) {
            IronGolem police = Main.getInstance().getPoliceSystem().getNearByPolice(player.getLocation());
            if (!Main.getInstance().getPoliceSystem().canPoliceSeePlayer(police, player)) {
                return;
            }

            Main.getInstance().getPoliceSystem().getPolices().put(player.getUniqueId(), police);
            police.setTarget(player);
            PlayerTitle.sendTitleToPlayer(player, ChatColor.BLUE + "경찰 앞에서 범죄를 저질렀습니다.", "명복을 빕니다");
        }

        npc.onPlayerViolation(player, new AssaultLaw());
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

        if (event.getEntity().getKiller() == null) {
            return;
        }

        Player killer = event.getEntity().getKiller();
        if (!Main.getInstance().getGameManager().getPlayers().contains(killer)) {
            return;
        }

        for (NPC npc : Main.getInstance().getNpcManager().getNearestVillagersWithinRange(deadVillager, killer, 10)) {
            if (npc != deadNPC) {
                Integer trustLevel = npc.getTrustLevel().get(killer);
                if (trustLevel != null) {
                    npc.updateTrust(killer, -20);
                    npc.onPlayerViolation(killer, new MurderLaw());
                }
                npc.updatePersonality(20);
            }
        }

        if (Main.getInstance().getQuestManager().getPlayerQuests(killer) == null) {
            killer.sendMessage("Your quest is null");
            return;
        }

        if (Main.getInstance().getQuestManager().hasQuestOfType(killer, QuestType.KILL)) {
            killer.sendMessage("You have kill quest.");
            Quest quest = Main.getInstance().getQuestManager().getQuestOfType(killer, QuestType.KILL);
            Main.getInstance().getQuestManager().endQuest(killer, quest, QuestEndReason.SUCCEED);
        }
    }

    private boolean isArson(Player player) {
        return player.getInventory().getItemInMainHand().getType().equals(Material.FLINT_AND_STEEL);
    }
}
