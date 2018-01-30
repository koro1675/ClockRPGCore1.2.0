package clockrpg.core.ClockRPGCoreListener;


import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class getDropItem implements Listener {

    @EventHandler
    public void onEnemyKill(EntityDeathEvent e){

        //死んだのがPlayerの場合
        if (e.getEntity().getType().equals(EntityType.PLAYER)){
            return;
        }

        //Entityが勝手に死んだ場合
        if (e.getEntity().getKiller() == null){
            return;
        }

        //playerが殺していない場合
        if (!e.getEntity().getKiller().getType().equals(EntityType.PLAYER)){
            return;
        }


        Player player = e.getEntity().getKiller();


        //Playerにドロップ品を与える
        for (ItemStack itemStack : e.getDrops()){
            player.getInventory().addItem(itemStack);
        }


        //落ちるアイテムの削除
        e.getDrops().clear();


        //経験値を与える
        player.giveExp(e.getDroppedExp());

        //落ちている経験値を消す
        e.setDroppedExp(0);
    }
}
