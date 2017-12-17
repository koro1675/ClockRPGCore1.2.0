package clockrpg.core.ClockRPGCoreListener;


import clockrpg.core.ClockRPGCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class skillLevel implements Listener {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);


    /*************************
     * 敵が死んだ場合
     *************************/
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        //playerに殺されていない場合
        if (e.getEntity().getKiller() == null){
            return;
        }

        //殺されたのがPlayerの場合
        if (e.getEntity().getType().equals(EntityType.PLAYER)){
            return;
        }

        //処理
        skillProcess(e.getEntity().getKiller());
    }



    /*************************
     * 熟練度処理
     *************************/
    void skillProcess(Player player){

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        //武器の種類
        String weaponTypeName = "";


        //itemMetaがない場合
        if (!itemStack.hasItemMeta()){
            return;
        }


        ItemMeta itemMeta = itemStack.getItemMeta();


        //手に持っているアイテムに武器名が指定されている場合
        for (weaponType weaponType : weaponType.values()){
            if (itemMeta.getLore().contains(weaponType.getValue())){
                weaponTypeName = weaponType.getValue();
            }
        }

        //loreから武器種を引く
        switch (weaponTypeName){
            case "§f§l武器: 剣":
                weaponTypeName = "sword";
                break;
            case "§f§l武器: 斧":
                weaponTypeName = "axe";
                break;
            default:
                return;
        }

        //kill数を1増やす
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET " + weaponTypeName.toUpperCase() + "KILL=?" + " WHERE UUID=?");
            statement.setInt(1, getSkillKills(player, weaponTypeName) + 1);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Kill数が次のレベルに上がるに値しない場合
        Integer killAmount = plugin.getConfig().getInt("config.killAmount");
        if ((getSkillLevel(player, weaponTypeName) * killAmount) >= getSkillKills(player, weaponTypeName)){
            return;
        }

        //playerの熟練度を1上げる
        setSkillLevel(player, weaponTypeName,getSkillLevel(player, weaponTypeName) + 1);

    }


    /*************************
     * player熟練度の設定
     *************************/
    public void setSkillLevel(Player player, String skillName, Integer amount){

        String weaponTypeName = "";
        String displayWeaponTypeName = "";

        //amountに-を指定できないようにする
        if (amount < 0){
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§3§lマイナス値は使用不可です");
            return;
        }

        //skillNameの確認
        switch (skillName.toUpperCase()) {
            case "SWORD":
                weaponTypeName = "sword";
                //weaponTypeNameをDisplay用に変換
                displayWeaponTypeName = "剣";
                break;
            case "AXE":
                weaponTypeName = "axe";
                displayWeaponTypeName = "斧";
                break;
            default:
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§3§lその引数は使えません");
                return;
        }

        //処理
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection()
                    .prepareStatement("UPDATE " + plugin.mysqlSetup.table + " SET " + weaponTypeName.toUpperCase() + "LEVEL=?, " + weaponTypeName.toUpperCase() + "KILL=?" + " WHERE UUID=?");
            statement.setInt(1, amount);
            statement.setInt(2, 0);
            statement.setString(3, player.getUniqueId().toString());
            statement.executeUpdate();


        } catch (SQLException e){
            e.printStackTrace();
        }

        //武器種熟練度から攻撃力を計算
        double attackDamage = getSkillLevel(player, weaponTypeName) * plugin.getConfig().getDouble("config.damageAmount");

        player.sendMessage(plugin.prefix + "§5§l" + displayWeaponTypeName + "熟練度が§6§l" + getSkillLevel(player, weaponTypeName) + "§5§lになりました！");
        player.sendMessage("§a§l現在の" + displayWeaponTypeName + "攻撃力: +" + attackDamage);
    }



    /*************************
     * player熟練度の取得
     *************************/
    int getSkillLevel(Player player, String skillName){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            result.next();

            switch (skillName.toLowerCase()){
                case "sword":
                    return result.getInt("SWORDLEVEL");
                case "axe":
                    return result.getInt("AXELEVEL");
                default:
                  return 0;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }



    /*************************
     * playerの敵のキル数の取得
     *************************/
    int getSkillKills(Player player, String skillName){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            result.next();

            switch (skillName.toLowerCase()){
                case "sword":
                    return result.getInt("SWORDKILL");
                case "axe":
                    return result.getInt("AXEKILL");
                default:
                    return 0;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }



    /*************************
     * 武器の種類
     *************************/
    public enum weaponType {
        sword("§f§l武器: 剣"),
        axe("§f§l武器: 斧");

        //フィールドの定義
        private String name;

        //コンストラクタの定義
        private weaponType(String name){
            this.name = name;
        }

        //メソッド
        public String getValue() {
            return this.name;
        }
    }

}
