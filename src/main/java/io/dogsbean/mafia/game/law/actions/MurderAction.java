package io.dogsbean.mafia.game.law.actions;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.Crime;
import io.dogsbean.mafia.game.law.Criminal;
import io.dogsbean.mafia.game.law.NPCAction;
import io.dogsbean.mafia.util.PlayerTitle;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MurderAction implements NPCAction {
    @Override
    public void execute(Player player) {
        Crime murder = new Crime("살인", "사람을 죽이는 범죄", 90);
        Criminal.commitCrime(player.getName(), murder);
        Main.getInstance().getPoliceSystem().reportPlayer(Main.getInstance().getNpcManager().getNearestVillagerWithinRange(player.getLocation(), 10), player);
    }
}
