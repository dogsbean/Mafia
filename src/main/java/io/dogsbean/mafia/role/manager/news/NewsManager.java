package io.dogsbean.mafia.role.manager.news;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NewsManager {
    public void publishNews(String headline, String content) {
        // 게임 내 뉴스 발행
        Bukkit.broadcastMessage("뉴스 제목: " + headline);
        Bukkit.broadcastMessage("내용: " + content);
    }

    public void publishFakeNews(Player criminal) {
        // 범죄자가 가짜 뉴스를 만들 때
    }
}
