package clockrpg.core.ClockRPGCoreListener;


import clockrpg.core.ClockRPGCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class updatePlayerAddress implements Listener {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);


    //IPの更新
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        updateIPAddless(e.getPlayer());
    }

    public void updateIPAddless(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET IP=? WHERE UUID=?");
            //IP
            statement.setString(1, player.getAddress().toString());

            //UUID
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
