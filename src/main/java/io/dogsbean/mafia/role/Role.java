package io.dogsbean.mafia.role;

import org.bukkit.entity.Player;

public abstract class Role {
    protected Player player;

    public Role(Player player) {
        this.player = player;
    }

    public abstract void performAction(); // 역할별 행동
}
