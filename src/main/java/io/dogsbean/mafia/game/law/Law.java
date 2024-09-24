package io.dogsbean.mafia.game.law;

import lombok.Getter;

public abstract class Law {
    @Getter private String description;
    @Getter private NPCAction npcAction;

    public Law(String description, NPCAction npcAction) {
        this.description = description;
        this.npcAction = npcAction;
    }
}
