package clockrpg.core;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClockRPGCorePlayer {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);


    /***********************
     * playerInfo
     ***********************/
    void playerInfo(Player sender, Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            result.next();

            //redPlayerの数値を見やすく変換
            String redPlayer = "";
            if (result.getInt("REDPLAYER") == 1){
                redPlayer = "true";
            } else {
                redPlayer = "false";
            }

            //blackPlayerの数値を見やすく変換
            String blackPlayer = "";
            if (result.getInt("BLACKPLAYER") == 1){
                blackPlayer = "true";
            } else {
                blackPlayer = "false";
            }

            //剣の攻撃力を取得
            double sword = result.getInt("SWORDLEVEL") * plugin.getConfig().getDouble("config.damageAmount");

            //斧の攻撃力を取得
            double axe = result.getInt("AXELEVEL") * plugin.getConfig().getDouble("config.damageAmount");


            //表示
            sender.sendMessage(plugin.prefix);
            sender.sendMessage("§7§m§l==================================================");
            sender.sendMessage("§l[§5§l" + player.getName() + "'s Player Data§f§l]");
            sender.sendMessage("§l|§7§lUUID: §5§l" + result.getString("UUID"));
            sender.sendMessage("§l|§7§lIP: §5§l" + result.getString("IP"));
            sender.sendMessage("§l|§7§lredPlayer: §5§l" + redPlayer);
            sender.sendMessage("§l|§7§lblackPlayer: §5§l" + blackPlayer);
            sender.sendMessage("§l|§7§lDisplayName: §r" + result.getString("DISPLAYNAME"));
            sender.sendMessage("§l|§7§lkillCounter: §5§l" + result.getInt("KILLCOUNTER"));
            sender.sendMessage("§l|§7§l剣熟練度: §5§l" + result.getInt("SWORDLEVEL"));
            sender.sendMessage("§l|§7§l斧熟練度: §5§l" + result.getInt("AXELEVEL"));
            sender.sendMessage("§l|§7§l剣攻撃力: §5§l+" + sword);
            sender.sendMessage("§l|§7§l斧攻撃力: §5§l+" + axe);
            sender.sendMessage("§7§m§l==================================================");


        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    /***********************
     * setRedPlayer
     ***********************/
    void setRedPlayer(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET BLACKPLAYER=?, REDPLAYER=?, DISPLAYNAME=? WHERE UUID=?");
            statement.setInt(1,0);
            statement.setInt(2, 1);
            statement.setString(3, "[§4§l♦§f]" + player.getName());
            statement.setString(4, player.getUniqueId().toString());
            statement.executeUpdate();

            player.setDisplayName("[§4§l♦§f]" + player.getName());
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんが§4§lレッドプレーヤー§3§lになりました");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /***********************
     * setBlackPlayer
     ***********************/
    void setBlackPlayer(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET BLACKPLAYER=?, REDPLAYER=?, DISPLAYNAME=? WHERE UUID=?");
            statement.setInt(1,1);
            statement.setInt(2, 1);
            statement.setString(3, "[§8§l♦§f]" + player.getName());
            statement.setString(4, player.getUniqueId().toString());
            statement.executeUpdate();

            player.setDisplayName("[§8§l♦§f]" + player.getName());
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんが§8§lブラックプレーヤー§3§lになりました");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /***********************
     * setNormalPlayer
     ***********************/
    void setNormalPlayer(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET BLACKPLAYER=?, REDPLAYER=?, DISPLAYNAME=? WHERE UUID=?");
            statement.setInt(1,0);
            statement.setInt(2, 0);
            statement.setString(3, "[§b§l♦§f]" + player.getName());
            statement.setString(4, player.getUniqueId().toString());
            statement.executeUpdate();

            player.setDisplayName("[§b§l♦§f]" + player.getName());
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんが§b§lノーマルプレーヤー§3§lになりました");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*******************
     * normalPlayerなら
     *******************/
    boolean isNormalPlayer(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet result = statement.executeQuery();
            result.next();

            //ランクの確認
            if (result.getInt("REDPLAYER") == 0){
                return true;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /*******************
     * redPlayerなら
     *******************/
    boolean isRedPlayer(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet result = statement.executeQuery();
            result.next();

            //ランクの確認
            if (result.getInt("REDPLAYER") == 1 && result.getInt("BLACKPLAYER") == 0){
                return true;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /*******************
     * blackPlayerなら
     *******************/
    boolean isBlackPlayer(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet result = statement.executeQuery();
            result.next();

            //ランクの確認
            if (result.getInt("BLACKPLAYER") == 1){
                return true;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
