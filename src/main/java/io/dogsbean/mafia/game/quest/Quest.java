package io.dogsbean.mafia.game.quest;

import lombok.Getter;
import org.bukkit.entity.Player;

public class Quest {
    @Getter private Player player;
    @Getter private QuestType type;

    public Quest(Player player, QuestType type) {
        this.player = player;
        this.type = type;
    }
}
