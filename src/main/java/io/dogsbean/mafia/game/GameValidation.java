package io.dogsbean.mafia.game;

import io.dogsbean.mafia.Main;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@UtilityClass
public final class GameValidation {

    private static final String ALREADY_JOINED = ChatColor.RED + "You are already joined game!";
    private static final String NOT_IN_GAME = ChatColor.RED + "You are not in the game!";
    private static final String GAME_IS_STARTED = ChatColor.RED + "You cannot do this!";

    private static final String CANNOT_START_PLAYERS = ChatColor.RED + "Couldn't start the game because there are less than 4 players.";
    private static final String CANNOT_STOP_GAME = ChatColor.RED + "Couldn't stop the game because there are no running games.";

    public static boolean isAbleToJoinGame(Player player) {
        boolean alreadyJoined = Main.getInstance().getGameManager().getPlayers().contains(player);
        boolean gameStarted = Main.getInstance().getGameManager().isGameInProgress();

        if (alreadyJoined) {
            player.sendMessage(ALREADY_JOINED);
            return false;
        }

        if (gameStarted) {
            player.sendMessage(GAME_IS_STARTED);
            return false;
        }

        player.sendMessage(ChatColor.GREEN + "You joined the game!");
        return true;
    }

    public static boolean isAbleToLeaveGame(Player player) {
        boolean isInGame = Main.getInstance().getGameManager().getPlayers().contains(player);
        boolean gameStarted = Main.getInstance().getGameManager().isGameInProgress();

        if (!isInGame) {
            player.sendMessage(NOT_IN_GAME);
            return false;
        }

        if (gameStarted) {
            player.sendMessage(GAME_IS_STARTED);
            return false;
        }

        player.sendMessage(ChatColor.RED + "You left the game!");
        return true;
    }

    public static boolean isAbleToStart(Player player) {
        boolean players = Main.getInstance().getGameManager().getPlayers().size() >= 4;
        boolean gameStarted = Main.getInstance().getGameManager().isGameInProgress();

        if (!players) {
            player.sendMessage(CANNOT_START_PLAYERS);
            return false;
        }

        if (gameStarted) {
            player.sendMessage(GAME_IS_STARTED);
            return false;
        }

        player.sendMessage(ChatColor.GREEN + "You started the game!");
        return true;
    }

    public static boolean isAbleToEnd(Player player) {
        boolean gameInProgress = Main.getInstance().getGameManager().isGameInProgress();

        if (!gameInProgress) {
            player.sendMessage(CANNOT_STOP_GAME);
            return false;
        }

        player.sendMessage(ChatColor.GREEN + "You stopped the game!");
        return true;
    }
}
