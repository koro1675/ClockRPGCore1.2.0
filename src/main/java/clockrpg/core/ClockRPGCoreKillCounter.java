package clockrpg.core;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClockRPGCoreKillCounter {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);


    /*******************
     * Kill数を増やす
     *******************/
    public void incrementKills(Player player, Integer amount){

        //マイナス値はできないようにする
        if (amount < 0){
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§3§lマイナスは設定できません");
            return;
        }

        //処理
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET KILLCOUNTER=? WHERE UUID=?");
            statement.setInt(1, getKills(player) + amount);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();

            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんのkill数が§4§l" + getKills(player) + "§3§lになりました");

            setPlayerRank(player);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*******************
     * Kill数を減らす
     *******************/
    public void decrementKills(Player player, Integer amount){

        //マイナス値はできないようにする
        if (amount < 0){
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§3§lマイナスは設定できません");
            return;
        }

        //kill数がマイナスになる場合0に設定
        if ((getKills(player) - amount) < 0){
            amount = getKills(player) * (-1);
        }

        //処理
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET KILLCOUNTER=? WHERE UUID=?");
            statement.setInt(1, getKills(player) - amount);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();

            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんのkill数が§4§l" + getKills(player) + "§3§lになりました");

            setPlayerRank(player);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*******************
     * Kill数をclear
     *******************/
    public void clearKills(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET KILLCOUNTER=? WHERE UUID=?");
            statement.setInt(1, 0);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();

            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんのkill数がクリアされました");

            setPlayerRank(player);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*******************
     * Kill数を設定する
     *******************/
    public void setKill(Player sender, Player player, Integer amount){

        //マイナスは設定できないようにする
        if (amount < 0){
            sender.sendMessage(plugin.prefix + "§3§lマイナスは設定できません");
            return;
        }

        //上限設定
        if (amount > 2147483647){
            sender.sendMessage(plugin.prefix + "§3§l2147483647以下にしてください");
            return;
        }

        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET KILLCOUNTER=? WHERE UUID=?");
            statement.setInt(1, amount);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();

            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんのキル数が§4§l" + amount + "§3§lになりました");

            setPlayerRank(player);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    /*******************
     * Kill数を取得
     *******************/
    public int getKills(Player player){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet result = statement.executeQuery();
            result.next();

            return result.getInt("KILLCOUNTER");

        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }



    /*******************
     * PlayerRank付け
     *******************/
    public void setPlayerRank(Player player){

        int amount = getKills(player);

        //kill数が3を下回るならnormalPlayerにする
        if (amount < 3){

            //もしすでにnormalPlayerの場合メッセージを変える
            if (plugin.clockRPGCorePlayer.isNormalPlayer(player)){
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんは現在§b§lノーマルプレーヤー§3§lです");
                return;
            }

            plugin.clockRPGCorePlayer.setNormalPlayer(player);
            return;
        }

        //kill数が3~5ならredPlayerにする
        if (amount >= 3 && amount <= 5){

            //もしすでにredPlayerの場合メッセージを変える
            if (plugin.clockRPGCorePlayer.isRedPlayer(player)){
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんは現在§4§lレッドプレーヤー§3§lです");
                return;
            }

            plugin.clockRPGCorePlayer.setRedPlayer(player);
            return;
        }

        //kill数が6以上ならBlackPlayerにする
        if (amount >= 6){

            //もしすでにblackPlayerの場合メッセージを変える
            if (plugin.clockRPGCorePlayer.isBlackPlayer(player)){
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + player.getName() + "§3§lさんは現在§8§lブラックプレーヤー§3§lです");
                return;
            }

            plugin.clockRPGCorePlayer.setBlackPlayer(player);
        }

    }
}
