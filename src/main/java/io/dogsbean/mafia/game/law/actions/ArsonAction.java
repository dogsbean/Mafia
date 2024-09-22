package io.dogsbean.mafia.game.law.actions;

import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.NPCAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArsonAction implements NPCAction {
    @Override
    public void execute(Player player) {
        player.sendMessage("방화다!"); // 해당 플레이어에게 메시지를 전송
    }
}
