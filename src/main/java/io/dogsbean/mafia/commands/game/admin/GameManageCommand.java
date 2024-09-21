package io.dogsbean.mafia.commands.game.admin;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.commands.PlayerCommand;
import io.dogsbean.mafia.game.GameEndReason;
import io.dogsbean.mafia.game.GameValidation;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameManageCommand extends PlayerCommand {
    private final Main plugin;

    public GameManageCommand(Main plugin) {
        super("gamemanage", "mafia.game");
        this.plugin = plugin;
        setAliases("gamemanage");
        setUsage(ChatColor.RED + "Usage: /gamemanage");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(usageMessage);
            return;
        }

        switch (args[1]) {
            case "start":
                if (GameValidation.isAbleToStart(player)) {
                    Main.getInstance().getGameManager().startGame();
                }
            break;
            case "stop":
                if (GameValidation.isAbleToEnd(player)) {
                    Main.getInstance().getGameManager().endGame("Game Ended.", GameEndReason.FORCE);
                }
                break;
        }
    }
}

