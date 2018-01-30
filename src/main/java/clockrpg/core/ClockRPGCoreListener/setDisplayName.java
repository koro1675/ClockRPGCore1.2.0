package clockrpg.core.ClockRPGCoreListener;


import clockrpg.core.ClockRPGCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class setDisplayName implements Listener {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        setDisplayName(e.getPlayer());
    }

    void setDisplayName(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            results.next();

            //displayNameを設定
            player.setDisplayName(results.getString("DISPLAYNAME"));

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}
