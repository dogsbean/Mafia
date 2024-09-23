package io.dogsbean.mafia.game.law.actions;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.Crime;
import io.dogsbean.mafia.game.law.Criminal;
import io.dogsbean.mafia.game.law.NPCAction;
import io.dogsbean.mafia.game.law.laws.AssaultLaw;
import io.dogsbean.mafia.game.police.PoliceSystem;
import io.dogsbean.mafia.npc.NPC;
import io.dogsbean.mafia.util.PlayerTitle;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AssaultAction implements NPCAction {
    @Override
    public void execute(Player player, NPC npc) {
        Crime assault = new Crime("폭행", "사람을 해하는 범죄", 5);
        Criminal.commitCrime(player.getName(), assault);

        NPC reportedNPC;
        NPC nearestNPC = Main.getInstance().getNpcManager().getNearestVillagerWithinRange(player.getLocation(), player, 10);

        if (nearestNPC == null) {
            reportedNPC = npc;
        } else {
            reportedNPC = nearestNPC;
        }

        Main.getInstance().getPoliceSystem().reportPlayer(reportedNPC, player, new AssaultLaw());
    }
}
