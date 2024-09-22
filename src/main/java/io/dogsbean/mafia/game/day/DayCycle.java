package io.dogsbean.mafia.game.day;

import io.dogsbean.mafia.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class DayCycle {
    private int day = 0;
    private final int totalDays = 7;
    private BukkitRunnable dayTask;

    public void start() {
        dayTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!Main.getInstance().getGameManager().isGameInProgress()) {
                    endGame();
                    cancel();
                    dayTask = null;
                    return;
                }

                day++;
                if (day > totalDays) {
                    endGame();
                    cancel();
                    dayTask = null;
                } else {
                    Bukkit.broadcastMessage("Day " + day + "이 시작되었습니다.");
                }
            }
        };
        dayTask.runTaskTimer(Main.getInstance(), 0L, 1200);
    }

    private void endGame() {
        Bukkit.broadcastMessage("게임이 종료되었습니다.");
    }

    public int getLeftDays() {
        return totalDays - day;
    }
}