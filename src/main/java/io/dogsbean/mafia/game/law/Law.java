package io.dogsbean.mafia.game.law;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Law {
    private String description;
    private NPCAction npcAction;

    public Law(String description, NPCAction npcAction) {
        this.description = description;
        this.npcAction = npcAction;
    }

    public String getDescription() {
        return description;
    }

    public void setNpcAction(NPCAction action) {
        npcAction = action;
    }

    public NPCAction getNpcAction() {
        return npcAction;
    }
}
