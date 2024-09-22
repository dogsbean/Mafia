package io.dogsbean.mafia.role;

import io.dogsbean.mafia.role.roles.CCTVWorker;
import io.dogsbean.mafia.role.roles.Mafia;
import org.bukkit.entity.Player;

import java.util.*;

public class RoleManager {
    private Map<Player, Role> playerRoles = new HashMap<>();

    public void assignRoles(List<Player> players) {
        playerRoles.clear();
        List<Role> roles = new ArrayList<>();

        int mafiaCount = Math.max(1, players.size() / 3);
        for (int i = 0; i < mafiaCount; i++) {
            roles.add(new Mafia(players.get(i)));
        }

        for (int i = mafiaCount; i < players.size(); i++) {
            roles.add(new CCTVWorker(players.get(i)));
        }

        Collections.shuffle(roles);

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Role role = roles.get(i);
            playerRoles.put(player, role);
            player.sendMessage("당신의 역할은: " + role.getClass().getSimpleName());
        }
    }

    public Role getRole(Player player) {
        return playerRoles.get(player);
    }
}
