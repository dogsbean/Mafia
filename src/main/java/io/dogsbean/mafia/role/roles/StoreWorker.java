package io.dogsbean.mafia.role.roles;

import io.dogsbean.mafia.role.Role;
import org.bukkit.entity.Player;

public class StoreWorker extends Role {
    public StoreWorker(Player player) {
        super(player);
    }

    @Override
    public void performAction() {
    }

    @Override
    public boolean isCitizen() {
        return true;
    }
}
