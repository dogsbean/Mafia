package io.dogsbean.mafia.npc.event;

import io.dogsbean.mafia.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TrustLevelChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final io.dogsbean.mafia.npc.NPC npc;
    private final int newTrustLevel;
    private final int oldTrustLevel;

    public TrustLevelChangeEvent(Player player, io.dogsbean.mafia.npc.NPC npc, int oldTrustLevel, int newTrustLevel) {
        this.player = player;
        this.npc = npc;
        this.oldTrustLevel = oldTrustLevel;
        this.newTrustLevel = newTrustLevel;
    }

    public Player getPlayer() {
        return player;
    }

    public NPC getNPC() {
        return npc;
    }

    public int getNewTrustLevel() {
        return newTrustLevel;
    }

    public int getOldTrustLevel() {
        return oldTrustLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
