package io.dogsbean.mafia.role;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Role {
    @Getter protected List<Player> players = new ArrayList<>();
    @Getter @Setter
    protected Player target;

    public Role(Player player) {
        this.players.add(player);
    }

    public abstract void performAction();

    public String getRole() {
        return this.getClass().getSimpleName();
    }

    public boolean isCitizen() {
        return false;
    }
}
