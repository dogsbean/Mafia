package io.dogsbean.mafia.commands.game;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.commands.PlayerCommand;
import io.dogsbean.mafia.game.GameValidation;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameCommand extends PlayerCommand {
    private final Main plugin;

    public GameCommand(Main plugin) {
        super("game");
        this.plugin = plugin;
        setAliases("game");
        setUsage(ChatColor.RED + "Usage: /game");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        switch (args[1]) {
            case "join":
                if (GameValidation.isAbleToJoinGame(player)) {
                    Main.getInstance().getGameManager().addPlayer(player);
                }
            break;
            case "leave":
                if (GameValidation.isAbleToLeaveGame(player)) {
                    Main.getInstance().getGameManager().removePlayer(player);
                }
                break;
        }
    }
}

