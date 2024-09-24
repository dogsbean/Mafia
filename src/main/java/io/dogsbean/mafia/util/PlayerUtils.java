package io.dogsbean.mafia.util;

import lombok.experimental.UtilityClass;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Iterator;

@UtilityClass
public class PlayerUtils {

    public static void reset(Player player, GameMode gameMode) {
        player.setWalkSpeed(0.2F);
        player.setHealth(player.getMaxHealth());
        player.setFallDistance(0.0F);
        player.setFoodLevel(20);
        player.setSaturation(10.0F);
        player.setLevel(0);
        player.setExp(0.0F);
        player.getInventory().clear();
        player.getInventory().setArmorContents((ItemStack[])null);
        player.setFireTicks(0);

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        if (gameMode != null && player.getGameMode() != gameMode) {
            player.setGameMode(gameMode);
        }

    }
}
