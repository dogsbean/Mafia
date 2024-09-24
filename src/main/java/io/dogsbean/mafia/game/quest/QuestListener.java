package io.dogsbean.mafia.game.quest;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.player.PlayerProfile;
import io.dogsbean.mafia.game.quest.event.QuestEndEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuestListener implements Listener {

    @EventHandler
    public void onQuestEnd(QuestEndEvent event) {
        if (event.getQuest() == null) {
            return;
        }

        Main.getInstance().getQuestManager().removeQuest(event.getQuest());

        if (event.getPlayer() == null) {
            return;
        }

        Player player = event.getPlayer();
        QuestEndReason reason = event.getReason();
        switch (reason) {
            case EXPIRED:
                player.sendMessage(ChatColor.RED + "Quest Failed! " + ChatColor.YELLOW + "\nQuest Expired.");
                break;
            case FAILED:
                player.sendMessage(ChatColor.RED + "Quest Failed! " + ChatColor.YELLOW + "\nYou didn't finished quest.");
                break;
            case SUCCEED:
                player.sendMessage(ChatColor.GREEN + "Quest Succeed! " + ChatColor.YELLOW + "\nYou finished quest!");
                PlayerProfile.getProfile(player).addMoney(event.getQuest().getType().getSucceedMoney());
        }
    }
}
