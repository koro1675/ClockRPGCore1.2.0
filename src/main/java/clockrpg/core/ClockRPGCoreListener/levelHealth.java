package clockrpg.core.ClockRPGCoreListener;


import clockrpg.core.ClockRPGCore;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class levelHealth implements Listener {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);


    /********************
     * レベルによるHP管理
     ********************/
    @EventHandler
    public void onLevelup(PlayerLevelChangeEvent e){
        setDataBaseLevel(e.getPlayer());
    }

    void setDataBaseLevel(Player player){

        //healthの変動
        setLevelHealth(player);

        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET LEVEL=? WHERE UUID=?");
            statement.setInt(1, player.getLevel());
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
            if (player.getLevel() % 10 == 0){
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§3§l" + player.getName() + "§8§lさんが§6§l" + player.getLevel() + "§8§lになりました！");
            }
        }catch (SQLException ee){
            ee.printStackTrace();
        }
    }

    //healthの設定
    void setLevelHealth(Player player){
        double health = player.getLevel() * 0.2 + 20;
        setAttribute(player, Attribute.GENERIC_MAX_HEALTH, health);
        player.sendMessage(plugin.prefix + "§5§lHPが§3§l" + health + "§5§lになりました！");
    }


    //Attributeの設定
    void setAttribute(Player player, org.bukkit.attribute.Attribute attribute, Double value){
        Attributable p = (Attributable) player;
        AttributeInstance ai = p.getAttribute(attribute);
        ai.setBaseValue(value);
    }

}
