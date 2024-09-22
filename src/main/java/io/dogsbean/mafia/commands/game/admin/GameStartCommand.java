package io.dogsbean.mafia.commands.game.admin;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.commands.PlayerCommand;
import io.dogsbean.mafia.game.GameEndReason;
import io.dogsbean.mafia.game.GameValidation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            Main.getInstance().getGameManager().startGame();
            Bukkit.broadcastMessage("Game Starting");
            return true;
        }
        return false;
    }
}

