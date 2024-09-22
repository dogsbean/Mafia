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
