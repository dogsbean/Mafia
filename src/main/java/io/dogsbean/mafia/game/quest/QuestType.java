package io.dogsbean.mafia.game.quest;

import io.dogsbean.mafia.role.Role;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum QuestType {

    KILL("Kil", "Kill the citizens.", Material.DIAMOND, 1000, 100, false, 4),
    SELL_THE_ITEM_FOR_FIRST_TIME("Sell the item for the first time", "Sell the item for the first time.", Material.GOLD_NUGGET, 1000, 80, false, 4),
    SURVIVE("Survive", "Do not get killed by mafia.", Material.GOLD_BLOCK, 0, 100, false, 7);

    @Getter private String name;
    @Getter private String description;
    @Getter private List<Role> roles = new ArrayList<>();
    @Getter private Material display;
    @Getter private int succeedMoney;
    @Getter private int priority;
    @Getter private boolean permanent;
    @Getter private int expireDay;

    QuestType(final String name, final String description, Material display, int succeedMoney, int priority, boolean permanent, int expireDay) {
        this.name = name;
        this.description = description;
        this.display = display;
        this.succeedMoney = succeedMoney;
        this.priority = priority;
        this.permanent = permanent;
        this.expireDay = expireDay;
    }

    public void addRoles(Role role) {
        this.roles.add(role);
    }

    public static QuestType getByName(final String input) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(input) || type.getName().equalsIgnoreCase(input)).findFirst().orElse(null);
    }
}