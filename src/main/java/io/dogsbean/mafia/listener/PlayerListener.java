package io.dogsbean.mafia.listener;

import io.dogsbean.mafia.role.Role;
import io.dogsbean.mafia.role.RoleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    private RoleManager roleManager;

    public PlayerListener(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Role role = roleManager.getRole(player);

        if (role != null) {
            role.performAction();
        }
    }
}
