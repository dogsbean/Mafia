package io.dogsbean.mafia.role.manager.day;

import io.dogsbean.mafia.Main;
import org.bukkit.Bukkit;

public class DayCycle {
    private int day = 0;
    private final int totalDays = 7;

    public void start() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                day++;
                if (day > totalDays) {
                    endGame(); // 게임 종료

                } else {
                    Bukkit.broadcastMessage("Day " + day + "이 시작되었습니다.");
                }
            }
        }, 0L, 1200);
    }

    private void endGame() {
        // 게임 종료 로직
        Bukkit.broadcastMessage("게임이 종료되었습니다.");
    }

    public int getLeftDays() {
        return totalDays - day;
    }
}