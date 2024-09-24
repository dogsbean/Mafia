package io.dogsbean.mafia.game.day;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.GameEndReason;
import io.dogsbean.mafia.game.law.Criminal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class DayCycle {
    private int day = 0;
    private final int totalDays = 7;
    private BukkitRunnable dayTask;

    public void start() {
        day = 0;
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

                Main.getInstance().getQuestManager().checkQuestExpiration(day);

                if (day > totalDays) {
                    endGame();
                    cancel();
                    dayTask = null;
                } else {
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Day " + day + "이 시작되었습니다.");
                    Bukkit.broadcastMessage("남은 일수: " + getLeftDays());
                    Bukkit.broadcastMessage("");

                    String mostWanted = Criminal.getMostWanted();
                    if (mostWanted != null) {
                        Main.getInstance().getNewsManager().publishNews(mostWanted + ", " + Criminal.getCrimes(mostWanted) + "을 저지르다.",
                                "경찰이 현재 수사 중입니다.");
                    }
                }
            }
        };
        dayTask.runTaskTimer(Main.getInstance(), 0L, 1200); // 60초마다 실행
    }

    private void endGame() {
        if (dayTask != null) {
            dayTask.cancel();
            dayTask = null;
        }

        Main.getInstance().getGameManager().endGame("Game Ended.", GameEndReason.MAFIA_ESCAPED);
        day = 0;
    }

    public int getLeftDays() {
        return totalDays - day;
    }
}