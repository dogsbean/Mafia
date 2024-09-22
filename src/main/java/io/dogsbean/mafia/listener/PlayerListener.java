package io.dogsbean.mafia.listener;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.GameEndReason;
import io.dogsbean.mafia.role.Role;
import io.dogsbean.mafia.role.RoleManager;
import io.dogsbean.mafia.role.roles.Mafia;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Role role = Main.getInstance().getRoleManager().getRole(player);

        if (role != null) {
            role.performAction();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!Main.getInstance().getGameManager().isGameInProgress()) {
            return;
        }

        Role role = Main.getInstance().getRoleManager().getRole(player);
        if (role == null) {
            return;
        }

        if (role.equals(new Mafia(player))) {
            if (Main.getInstance().getGameManager().getRemainingPlayersByRole().get("Mafia") < 1) {
                Main.getInstance().getGameManager().endGame("Game Ended.", GameEndReason.MAFIA_DEFEATED);
            } else {
                Bukkit.broadcastMessage(ChatColor.RED + "마피아가 사망했습니다. 남은 마피아: " + Main.getInstance().getGameManager().getRemainingPlayersByRole().get("Mafia"));
            }
        } else {
            if (Main.getInstance().getGameManager().getRemainingCitizens() < 1) {
                Main.getInstance().getGameManager().endGame("Game Ended.", GameEndReason.CITIZENS_DEFEATED);
            } else {
                Bukkit.broadcastMessage(ChatColor.RED + "시민이 사망했습니다. 남은 시민: " + Main.getInstance().getGameManager().getRemainingCitizens());
            }
        }
    }
}
