package io.dogsbean.mafia.role;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RoleManager {
    private Map<Player, Role> playerRoles = new HashMap<>();

    public void assignRoles() {
    }

    public Role getRole(Player player) {
        return playerRoles.get(player);
    }
}
