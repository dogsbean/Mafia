package io.dogsbean.mafia.game.quest;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.quest.event.QuestEndEvent;
import io.dogsbean.mafia.role.Role;
import io.dogsbean.mafia.role.roles.CCTVWorker;
import io.dogsbean.mafia.role.roles.Mafia;
import io.dogsbean.mafia.role.roles.StoreWorker;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestManager {
    @Getter private List<Quest> quests;
    @Getter private Map<Quest, BukkitTask> tasks;

    public QuestManager() {
        quests = new ArrayList<>();
        tasks = new HashMap<>();
    }

    public void addQuest(Quest quest) {
        quests.add(quest);
    }

    public void removeQuest(Quest quest) {
        quests.remove(quest);
    }

    public void checkQuestExpiration(int currentDay) {
        List<Quest> expiredQuests = new ArrayList<>();

        for (Quest quest : quests) {
            int remainingDays = getRemainingDays(quest, currentDay);
            if (!quest.getType().isPermanent() && remainingDays <= 0) {
                expiredQuests.add(quest);
            }
        }

        for (Quest quest : expiredQuests) {
            endQuest(quest.getPlayer(), quest, QuestEndReason.EXPIRED);

            quest.getType().getRoles().forEach(role -> {
                role.getPlayers().forEach(player ->
                        player.sendMessage(quest.getType().getName() + " 퀘스트가 만료되었습니다. 퀘스트 실패")
                );
            });
        }
    }

    public void endQuest(Player player, Quest quest, QuestEndReason reason) {
        QuestEndEvent event = new QuestEndEvent(player, quest, reason);
        Bukkit.getPluginManager().callEvent(event);
    }

    public int getRemainingDays(Quest quest, int currentDay) {
        if (quest.getType().isPermanent()) {
            return -1;
        }
        return quest.getType().getExpireDay() - currentDay;
    }

    public void initializeQuest(Player player) {
        if (Main.getInstance().getRoleManager().getRole(player) == null) {
            player.sendMessage(ChatColor.RED + "Couldn't initialize quest since your role is null.");
            return;
        }

        Role role = Main.getInstance().getRoleManager().getRole(player);

        if (role.isCitizen()) {
            QuestType questType = QuestType.SURVIVE;
            questType.addRoles(new StoreWorker(player));
            questType.addRoles(new CCTVWorker(player));
            Quest quest = new Quest(player, questType);
            addQuest(quest);
        }

        if (role instanceof Mafia) {
            QuestType questType = QuestType.KILL;
            questType.addRoles(new Mafia(player));
            Quest quest = new Quest(player, questType);
            addQuest(quest);
            return;
        }

        if (role instanceof StoreWorker) {
            QuestType questType = QuestType.SELL_THE_ITEM_FOR_FIRST_TIME;
            questType.addRoles(new StoreWorker(player));
            Quest quest = new Quest(player, questType);
            addQuest(quest);
        }
    }

    public List<Quest> getPlayerQuests(Player player) {
        List<Quest> playerQuests = new ArrayList<>();
        for (Quest quest : quests) {
            if (quest.getPlayer().equals(player)) {
                playerQuests.add(quest);
            }
        }
        return playerQuests;
    }

    public boolean hasQuestOfType(Player player, QuestType questType) {
        for (Quest quest : quests) {
            if (quest.getPlayer().equals(player) && quest.getType() == questType) {
                return true;
            }
        }
        return false;
    }

    public Quest getQuestOfType(Player player, QuestType questType) {
        for (Quest quest : quests) {
            if (quest.getPlayer().equals(player) && quest.getType() == questType) {
                return quest;
            }
        }
        return null;
    }
}
