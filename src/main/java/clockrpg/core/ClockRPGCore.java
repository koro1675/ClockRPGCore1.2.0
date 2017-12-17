package clockrpg.core;

import clockrpg.core.ClockRPGCoreListener.*;
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
        getServer().getPluginManager().registerEvents(new setDisplayName(),this);
        getServer().getPluginManager().registerEvents(new skillLevel(),this);
        getServer().getPluginManager().registerEvents(new killCounter(), this);
        getServer().getPluginManager().registerEvents(new updatePlayerAddress(), this);
        getServer().getPluginManager().registerEvents(new levelHealth(), this);
        getServer().getPluginManager().registerEvents(new createPlayerData(), this);
        getServer().getPluginManager().registerEvents(new attackPowerPreference(), this);
        getServer().getPluginManager().registerEvents(new getDropItem(), this);


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
