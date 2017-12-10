package clockrpg.core.ClockRPGCoreListener;


import clockrpg.core.ClockRPGCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class createPlayerData implements Listener {


    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        createPlayer(player.getUniqueId(), player);
    }


    public boolean playerExists(Player player, UUID uuid) {
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                plugin.getServer().getLogger().info(player.getName() + ChatColor.GREEN + " FoundPlayerData");
                return true;
            }
            plugin.getServer().getLogger().info(player.getName() + ChatColor.RED + " PlayerNotFound, CreatingPlayerData...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void createPlayer(final UUID uuid, Player player) {
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            results.next();


            // データの確認
            if (!playerExists(player, uuid)) {
                PreparedStatement insert = plugin.mysqlSetup.getConnection()
                        .prepareStatement("INSERT INTO " + plugin.mysqlSetup.table +
                                " (UUID,NAME,LEVEL,DISPLAYNAME,AXEKILL,SWORDKILL,AXELEVEL,SWORDLEVEL,KILLCOUNTER,REDPLAYER,BLACKPLAYER,IP)" +
                                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");

                //UUID
                insert.setString(1, uuid.toString());

                //Name
                insert.setString(2, player.getName());

                //level
                insert.setInt(3, player.getLevel());

                //displayName
                insert.setString(4, "[§b§l♦§f]" + player.getName());

                //axeKill
                insert.setInt(5,0);

                //swordKill
                insert.setInt(6,0);

                //axeLevel
                insert.setInt(7,1);

                //swordLevel
                insert.setInt(8,1);

                //killCounter
                insert.setInt(9,0);

                //redPlayer
                insert.setInt(10,0);

                //blackPlayer
                insert.setInt(11, 0);

                //IPAddress
                insert.setString(12, player.getAddress().toString());


                insert.executeUpdate();


                plugin.getServer().getLogger().info(player.getName() + "§e§lPlayerDataCreated");

            }

        } catch (SQLException e) {
            plugin.getServer().getLogger().info("§4§lError! Cannot Create Player Data!");
            e.printStackTrace();
        }
    }
}
