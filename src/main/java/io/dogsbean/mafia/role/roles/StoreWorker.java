package io.dogsbean.mafia.role.roles;

import io.dogsbean.mafia.role.Role;
import io.dogsbean.mafia.role.roles.menu.ShopMenu;
import org.bukkit.entity.Player;

public class StoreWorker extends Role {
    public StoreWorker(Player player) {
        super(player);
    }

    @Override
    public void performAction() {
        new ShopMenu().openShopMenu(target);
    }

    @Override
    public boolean isCitizen() {
        return true;
    }
}
