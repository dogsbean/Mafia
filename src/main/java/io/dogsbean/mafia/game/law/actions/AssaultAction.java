package io.dogsbean.mafia.game.law.actions;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.NPCAction;
import io.dogsbean.mafia.game.police.PoliceSystem;
import org.bukkit.entity.Player;

public class AssaultAction implements NPCAction {
    @Override
    public void execute(Player player) {
        player.sendMessage("폭행이다!"); // 해당 플레이어에게 메시지를 전송
        Main.getInstance().getPoliceSystem().reportPlayer(Main.getInstance().getNpcManager().getNearestVillagerWithinRange(player.getLocation(), 10), player);
    }
}
