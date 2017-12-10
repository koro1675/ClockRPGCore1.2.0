package clockrpg.core;

import clockrpg.core.mysql.mysqlSetup;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClockRPGCore extends JavaPlugin {

    public String prefix = "§f§l[§8§lClock§8§lRPG§5§lCore§f§l]§r";

    //Class定義
    public ClockRPGCorePermissions clockRPGCorePermissions;
    public ClockRPGCoreKillCounter clockRPGCoreKillCounter;
    public ClockRPGCorePlayer clockRPGCorePlayer;
    public mysqlSetup mysqlSetup;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Class初期化
        mysqlSetup = new mysqlSetup();
        clockRPGCorePlayer = new ClockRPGCorePlayer();
        clockRPGCoreKillCounter = new ClockRPGCoreKillCounter();
        clockRPGCorePermissions = new ClockRPGCorePermissions();

        //Command定義
        getCommand("crpg").setExecutor(new ClockRPGCoreCommands());


        //Listener定義
        getServer().getPluginManager().registerEvents(new clockrpg.core.ClockRPGCoreListener.setDisplayName(),this);
        getServer().getPluginManager().registerEvents(new clockrpg.core.ClockRPGCoreListener.skillLevel(),this);
        getServer().getPluginManager().registerEvents(new clockrpg.core.ClockRPGCoreListener.killCounter(), this);
        getServer().getPluginManager().registerEvents(new clockrpg.core.ClockRPGCoreListener.updatePlayerAddress(), this);
        getServer().getPluginManager().registerEvents(new clockrpg.core.ClockRPGCoreListener.levelHealth(), this);
        getServer().getPluginManager().registerEvents(new clockrpg.core.ClockRPGCoreListener.createPlayerData(), this);


        //データベースのセットアップ
        mysqlSetup.mysqlSetup();

        //configファイルの生成
        this.saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
