package io.dogsbean.mafia.commands.game.admin;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.commands.PlayerCommand;
import io.dogsbean.mafia.game.GameEndReason;
import io.dogsbean.mafia.game.GameValidation;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameStopCommand extends PlayerCommand {
    private final Main plugin;

    public GameStopCommand(Main plugin) {
        super("gamemanage stop", "mafia.game");
        this.plugin = plugin;
        setAliases("gamemanage stop");
        setUsage(ChatColor.RED + "Usage: /gamemanage stop");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(usageMessage);
            return;
        }

        if (GameValidation.isAbleToEnd(player)) {
            Main.getInstance().getGameManager().endGame("Game Ended.", GameEndReason.FORCE);
        }
    }
}

