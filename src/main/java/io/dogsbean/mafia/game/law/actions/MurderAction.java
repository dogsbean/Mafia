package io.dogsbean.mafia.game.law.actions;

import io.dogsbean.mafia.game.law.NPCAction;
import org.bukkit.entity.Player;

public class MurderAction implements NPCAction {
    @Override
    public void execute(Player player) {
        player.sendMessage("살인이다!"); // 해당 플레이어에게 메시지를 전송
    }
}
