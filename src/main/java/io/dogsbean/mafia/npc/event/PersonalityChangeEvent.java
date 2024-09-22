package io.dogsbean.mafia.npc.event;

import io.dogsbean.mafia.npc.NPC;
import io.dogsbean.mafia.npc.Personality;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PersonalityChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final NPC npc;
    private final Personality newPersonality;
    private final Personality oldPersonality;

    public PersonalityChangeEvent(NPC npc, Personality newPersonality, Personality oldPersonality) {
        this.npc = npc;
        this.oldPersonality = oldPersonality;
        this.newPersonality = newPersonality;
    }

    public NPC getNPC() {
        return npc;
    }

    public Personality getNewPersonality() {
        return newPersonality;
    }

    public Personality getOldPersonality() {
        return oldPersonality;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
