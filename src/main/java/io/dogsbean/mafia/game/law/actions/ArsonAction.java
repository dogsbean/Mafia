package io.dogsbean.mafia.game.law.actions;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.Crime;
import io.dogsbean.mafia.game.law.Criminal;
import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.NPCAction;
import io.dogsbean.mafia.util.PlayerTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArsonAction implements NPCAction {
    @Override
    public void execute(Player player) {
        PlayerTitle.sendTitleToPlayer(player, ChatColor.RED + "방화 신고", ChatColor.YELLOW + "시민이 당신을 신고했습니다.");
        Crime arson = new Crime("방화", "불을 지르는 범죄", 20);
        Criminal.commitCrime(player.getName(), arson);
        Main.getInstance().getPoliceSystem().reportPlayer(Main.getInstance().getNpcManager().getNearestVillagerWithinRange(player.getLocation(), 10), player);
    }
}
