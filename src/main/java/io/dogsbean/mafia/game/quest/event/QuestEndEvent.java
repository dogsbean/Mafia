package io.dogsbean.mafia.game.quest.event;

import io.dogsbean.mafia.game.quest.Quest;
import io.dogsbean.mafia.game.quest.QuestEndReason;
import io.dogsbean.mafia.npc.NPC;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuestEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter private final Player player;
    @Getter private final Quest quest;
    @Getter private final QuestEndReason reason;

    public QuestEndEvent(Player player, Quest quest, QuestEndReason reason) {
        this.player = player;
        this.quest = quest;
        this.reason = reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
