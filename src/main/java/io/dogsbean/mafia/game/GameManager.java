package io.dogsbean.mafia.game;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.npc.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.*;

public class GameManager {
    private List<Player> players;
    private NPCManager npcManager;
    private World world;
    private boolean gameInProgress;

    public GameManager(World world) {
        this.world = world;
        this.players = new ArrayList<>();
        this.npcManager = new NPCManager();
        this.gameInProgress = false;
    }

    public void startGame() {
        if (gameInProgress) {
            return;
        }

        gameInProgress = true;
        players.clear();
        npcManager.loadVillagers(world);
        Main.getInstance().getRoleManager().assignRoles();
        Main.getInstance().getDayCycle().start();

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this::gameLoop, 0, 1200); // 60초마다 호출
    }

    private void gameLoop() {
        if (!gameInProgress) {
            return;
        }

        Bukkit.broadcastMessage("남은 일수: " + Main.getInstance().getDayCycle().getLeftDays());
    }

    public void endGame(String message,GameEndReason reason) {
        gameInProgress = false;
        Bukkit.broadcastMessage(message);
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            player.sendMessage("게임에 참가하셨습니다!");
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.sendMessage("게임에서 나가셨습니다.");
    }

    public List<Player> getPlayers() {
        return players;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }
}
