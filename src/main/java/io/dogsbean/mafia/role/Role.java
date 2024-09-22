package io.dogsbean.mafia.role;

import org.bukkit.entity.Player;

public abstract class Role {
    protected Player player;
    protected Player target;

    public Role(Player player) {
        this.player = player;
    }

    public abstract void performAction();

    public String getRole() {
        return this.getClass().getSimpleName(); // 클래스 이름을 통해 역할 반환
    }

    public boolean isCitizen() {
        return false;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Player getTarget() {
        return this.target;
    }
}
