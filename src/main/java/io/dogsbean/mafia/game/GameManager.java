package io.dogsbean.mafia.game;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.npc.NPCManager;
import io.dogsbean.mafia.role.Role;
import io.dogsbean.mafia.role.roles.Mafia;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.*;

public class GameManager {
    private List<Player> players = new ArrayList<>();
    private NPCManager npcManager;
    private World world;
    private boolean gameInProgress;

    public GameManager(World world) {
        this.world = world;
        this.players = new ArrayList<>();
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

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this::gameLoop, 0, 1200); // 60초마다 호출
    }

    private void gameLoop() {
        if (!gameInProgress) {
            return;
        }

        Bukkit.broadcastMessage("남은 일수: " + Main.getInstance().getDayCycle().getLeftDays());
    }

    public void endGame(String message, GameEndReason reason) {
        gameInProgress = false;

        // NPC 제거
        npcManager.removeAllVillagers();

        // 게임 종료 메시지
        Bukkit.broadcastMessage(message);

        // 각 플레이어에게 결과 및 역할 알리기
        for (Player player : players) {
            Role role = Main.getInstance().getRoleManager().getRole(player);
            String resultMessage = getResultMessage(role, reason);
            player.sendMessage(resultMessage);
        }

        // 플레이어 목록 초기화
        players.clear();
    }

    private String getResultMessage(Role role, GameEndReason reason) {
        // 결과 메시지를 정의합니다. 예를 들어, 역할에 따라 다르게 알림.
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

    public Map<String, Integer> getRemainingPlayersByRole() {
        Map<String, Integer> roleCount = new HashMap<>();

        for (Player player : players) {
            Role role = Main.getInstance().getRoleManager().getRole(player);
            String roleName = role.getRole();

            roleCount.put(roleName, roleCount.getOrDefault(roleName, 0) + 1);
        }

        return roleCount;
    }

    public int getRemainingCitizens() {
        int citizenCount = 0;

        for (Player player : players) {
            Role role = Main.getInstance().getRoleManager().getRole(player);
            if (role.isCitizen()) { // isCitizen 메소드 사용
                citizenCount++;
            }
        }

        return citizenCount;
    }
}
