package io.dogsbean.mafia.game.law.actions;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.Crime;
import io.dogsbean.mafia.game.law.Criminal;
import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.NPCAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArsonAction implements NPCAction {
    @Override
    public void execute(Player player) {
        player.sendMessage("방화다!"); // 해당 플레이어에게 메시지를 전송
        Crime arson = new Crime("방화", "불을 지르는 범죄", 20);
        Criminal.commitCrime(player.getName(), arson);
        Main.getInstance().getPoliceSystem().reportPlayer(Main.getInstance().getNpcManager().getNearestVillagerWithinRange(player.getLocation(), 10), player);
    }
}
