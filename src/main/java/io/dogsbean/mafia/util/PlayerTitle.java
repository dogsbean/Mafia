package io.dogsbean.mafia.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Utility;
import org.bukkit.entity.Player;

@UtilityClass
public class PlayerTitle {

    public static void sendTitleToPlayer(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 10, 70, 20);
    }
}
