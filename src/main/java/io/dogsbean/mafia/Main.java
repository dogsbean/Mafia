package io.dogsbean.mafia;

import io.dogsbean.mafia.commands.game.admin.GameStartCommand;
import io.dogsbean.mafia.commands.game.admin.GameStopCommand;
import io.dogsbean.mafia.game.GameManager;
import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.LawManager;
import io.dogsbean.mafia.game.law.listener.PlayerLawListener;
import io.dogsbean.mafia.game.police.PoliceSystem;
import io.dogsbean.mafia.game.quest.QuestListener;
import io.dogsbean.mafia.game.quest.QuestManager;
import io.dogsbean.mafia.listener.BasicPreventionListener;
import io.dogsbean.mafia.listener.NPCListener;
import io.dogsbean.mafia.listener.PlayerListener;
import io.dogsbean.mafia.npc.NPCManager;
import io.dogsbean.mafia.role.RoleManager;
import io.dogsbean.mafia.role.manager.cctv.CCTVManager;
import io.dogsbean.mafia.game.day.DayCycle;
import io.dogsbean.mafia.role.manager.news.NewsManager;
import io.dogsbean.mafia.role.roles.menu.ShopMenu;
import io.dogsbean.mafia.util.LlamaAPI;
import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.utils.PromptBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Getter private static Main instance;

    @Getter private OllamaAPI ollamaAPI;
    @Getter private PromptBuilder promptBuilder;
    @Getter private LlamaAPI llamaAPI;

    @Getter private RoleManager roleManager;
    @Getter private NPCManager npcManager;
    @Getter private CCTVManager cctvManager;
    @Getter private NewsManager newsManager;
    @Getter private GameManager gameManager;
    @Getter private LawManager lawManager;
    @Getter private PoliceSystem policeSystem;
    @Getter private QuestManager questManager;
    @Getter private DayCycle dayCycle;

    @Override
    public void onEnable() {
        instance = this;

        ollamaAPI = new OllamaAPI("http://localhost:11434/");
        ollamaAPI.setRequestTimeoutSeconds(2000);
        promptBuilder = new PromptBuilder();
        llamaAPI = new LlamaAPI();
        roleManager = new RoleManager();
        npcManager = new NPCManager();
        cctvManager = new CCTVManager();
        newsManager = new NewsManager();
        lawManager = new LawManager();
        dayCycle = new DayCycle();
        policeSystem = new PoliceSystem();
        questManager = new QuestManager();
        gameManager = new GameManager(getServer().getWorld("world"));

        Bukkit.getPluginManager().registerEvents(new NPCListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new BasicPreventionListener(), this);

        Bukkit.getPluginManager().registerEvents(new QuestListener(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerLawListener(), this);

        Bukkit.getPluginManager().registerEvents(new ShopMenu(), this);

        getCommand("start").setExecutor(new GameStartCommand());
        getCommand("end").setExecutor(new GameStopCommand());
    }

    @Override
    public void onDisable() {
        npcManager.removeAllVillagers();
        Bukkit.getOnlinePlayers().forEach(player -> policeSystem.clear(player));
    }
}
