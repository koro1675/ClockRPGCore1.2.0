package clockrpg.core.ClockRPGCoreListener;


import clockrpg.core.ClockRPGCore;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class attackPowerPreference implements Listener {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);



    /*************************
     * 熟練度から攻撃力を設定する
     *************************/
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e){

        //ダメージを与えたのがPlayerでない場合
        if (!e.getDamager().getType().equals(EntityType.PLAYER)){
            return;
        }

        Player damager = (Player) e.getDamager();

        //武器種に指定されたアイテムを持っていない場合
        if (getUseWeaponType(damager) == null){
            return;
        }

        //処理
        setSkillLevelPower(damager, getUseWeaponType(damager));

    }

    void setSkillLevelPower(Player player, String weaponTypeName){
        try {
            PreparedStatement statement = plugin.mysqlSetup.getConnection().prepareStatement("SELECT * FROM " + plugin.mysqlSetup.table + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            result.next();


            //weaponTypeNameを変換
            String weaponType = "";

            if (weaponTypeName.equals("§f§l武器: 剣")){
                weaponType = "sword";
            } else if (weaponTypeName.equals("§f§l武器: 斧")){
                weaponType = "axe";
            }

            //武器種熟練度
            int skillLevel = result.getInt(weaponType.toUpperCase() + "LEVEL");

            //武器種熟練度から攻撃力を計算
            double attackDamage = skillLevel * plugin.getConfig().getDouble("config.damageAmount");

            //攻撃力のセット
            setAttribute(player, Attribute.GENERIC_ATTACK_DAMAGE, attackDamage);


        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Attributeの設定
    void setAttribute(Player player, org.bukkit.attribute.Attribute attribute, Double value){
        Attributable p = (Attributable) player;
        AttributeInstance ai = p.getAttribute(attribute);
        ai.setBaseValue(value);
        player.sendMessage(plugin.prefix + value);
    }


    //アイテムから武器種を引く
    String getUseWeaponType(Player player){

        //手持ちのアイテムを取得
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        //nullの場合
        if (itemStack == null){
            return null;
        }

        //ItemMetaを持っていない場合
        if (!itemStack.hasItemMeta()){
            return null;
        }


        //手持ちのItemMetaを取得
        ItemMeta itemMeta = itemStack.getItemMeta();

        //武器種分回す
        for (weaponType weaponType : weaponType.values()){

             //武器に武器種が指定されている場合
            if (itemMeta.getLore().contains(weaponType.getValue())){
                return weaponType.getValue();
            }
        }


        return null;
    }

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
