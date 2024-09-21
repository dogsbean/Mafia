package io.dogsbean.mafia.role.roles;

import io.dogsbean.mafia.role.Role;
import org.bukkit.entity.Player;

public class Mafia extends Role {
    public Mafia(Player player) {
        super(player);
    }

    @Override
    public void performAction() {
        // 범죄자가 할 수 있는 행동 정의
    }
}
