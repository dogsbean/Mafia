package io.dogsbean.mafia.game.law;

import io.dogsbean.mafia.npc.NPC;
import org.bukkit.entity.Player;

public interface NPCAction {
    void execute(Player player, NPC npc);
}
