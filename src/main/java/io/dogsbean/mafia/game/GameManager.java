package io.dogsbean.mafia.game;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.Criminal;
import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.actions.ArsonAction;
import io.dogsbean.mafia.game.player.PlayerProfile;
import io.dogsbean.mafia.game.police.PoliceSystem;
import io.dogsbean.mafia.npc.NPCManager;
import io.dogsbean.mafia.role.Role;
import io.dogsbean.mafia.role.roles.Mafia;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.*;

public class GameManager {
    private List<Player> players = new ArrayList<>();
    private List<Player> spectators = new ArrayList<>();
    private NPCManager npcManager;
    private World world;
    private boolean gameInProgress;

    public GameManager(World world) {
        this.world = world;
        this.npcManager = Main.getInstance().getNpcManager();
        this.gameInProgress = false;
    }

    public void startGame() {
        if (gameInProgress) {
            return;
        }

        gameInProgress = true;
        players.addAll(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            npcManager.loadVillagers(world, player);
        }
        Main.getInstance().getRoleManager().assignRoles(players);
        Main.getInstance().getDayCycle().start();
    }

    public void endGame(String message, GameEndReason reason) {
        gameInProgress = false;

        npcManager.removeAllVillagers();
        players.forEach(player -> Main.getInstance().getPoliceSystem().clear(player));
        Bukkit.broadcastMessage(message);

        for (Player player : players) {
            Role role = Main.getInstance().getRoleManager().getRole(player);
            String resultMessage = getResultMessage(role, reason);
            player.sendMessage(resultMessage);
        }

        spectators.forEach(spectator -> spectator.setGameMode(GameMode.SURVIVAL));

        List<Player> playersToRemove = new ArrayList<>(players);
        for (Player player : playersToRemove) {
            removePlayer(player);
        }

        players.clear();
        spectators.clear();
        Criminal.clearCrimes();
    }

    private String getResultMessage(Role role, GameEndReason reason) {
        if (reason == GameEndReason.CITIZENS_DEFEATED) {
            if (role instanceof Mafia) {
                return "축하합니다! 당신은 범죄자로서 승리했습니다.";
            } else {
                return "안타깝습니다. 당신은 범죄자를 잡지 못했습니다.";
            }
        } else if (reason == GameEndReason.MAFIA_DEFEATED) {
            if (role instanceof Mafia) {
                return "아쉽습니다. 범죄자로서 패배했습니다.";
            } else {
                return "축하합니다! 범죄자를 잡아냈습니다.";
            }
        } else if (reason == GameEndReason.FORCE) {
            return "게임이 강제 종료 되었습니다..";
        }
        return "게임이 종료되었습니다.";
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            player.sendMessage("게임에 참가하셨습니다!");
        }
    }

    public void removePlayer(Player player) {
        if (player != null) {
            players.remove(player);
            PlayerProfile playerProfile = PlayerProfile.getProfile(player);
            playerProfile.initializeProfile();
            Main.getInstance().getRoleManager().getPlayerRoles().remove(player);
        }
    }

    public void addSpectator(Player player, boolean gameEnded) {
        if (!spectators.contains(player) && !players.contains(player)) {
            spectators.add(player);
            player.setGameMode(GameMode.SPECTATOR);
            if (!gameEnded) player.sendMessage("당신은 이제 관전자로 추가되었습니다.");
        }
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

    public Map<String, Integer> getRemainingPlayersByRole() {
        Map<String, Integer> roleCount = new HashMap<>();

        for (Player player : players) {
            Role role = Main.getInstance().getRoleManager().getRole(player);
            if (role != null) {
                String roleName = role.getRole();
                roleCount.put(roleName, roleCount.getOrDefault(roleName, 0) + 1);
            }
        }

        return roleCount;
    }

    public int getRemainingCitizens() {
        int citizenCount = 0;

        for (Player player : players) {
            Role role = Main.getInstance().getRoleManager().getRole(player);
            if (role.isCitizen()) {
                citizenCount++;
            }
        }

        return citizenCount;
    }
}
