package io.dogsbean.mafia.game.law.actions;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.Crime;
import io.dogsbean.mafia.game.law.Criminal;
import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.NPCAction;
import io.dogsbean.mafia.game.law.laws.ArsonLaw;
import io.dogsbean.mafia.util.PlayerTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArsonAction implements NPCAction {
    @Override
    public void execute(Player player) {
        Crime arson = new Crime("방화", "불을 지르는 범죄", 20);
        Criminal.commitCrime(player.getName(), arson);
        Main.getInstance().getPoliceSystem().reportPlayer(Main.getInstance().getNpcManager().getNearestVillagerWithinRange(player.getLocation(), player, 10), player, new ArsonLaw());
    }
}
