package io.dogsbean.mafia.role.manager.news;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NewsManager {
    public void publishNews(String headline, String content) {
        Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "뉴스 제목: " + ChatColor.RESET + ChatColor.UNDERLINE + headline);
        Bukkit.broadcastMessage("내용: " + content);
    }

    public void publishFakeNews(Player criminal) {
    }
}
