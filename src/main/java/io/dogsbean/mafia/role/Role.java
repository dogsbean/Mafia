package io.dogsbean.mafia.role;

import org.bukkit.entity.Player;

public abstract class Role {
    protected Player player;

    public Role(Player player) {
        this.player = player;
    }

    public abstract void performAction(); // 역할별 행동

    public String getRole() {
        return this.getClass().getSimpleName(); // 클래스 이름을 통해 역할 반환
    }

    public boolean isCitizen() {
        return false;
    }
}
