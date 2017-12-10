package clockrpg.core.ClockRPGCoreListener;


import clockrpg.core.ClockRPGCore;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class killCounter implements Listener {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);


    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        //人が勝手に死んだ場合
        if (event.getEntity().getKiller() == null){
            return;
        }

        //人ではないEntityに殺された場合
        if (!event.getEntity().getKiller().getType().equals(EntityType.PLAYER)){
            return;
        }

        //処理
        plugin.clockRPGCoreKillCounter.incrementKills(event.getEntity().getKiller(), 1);
    }
}
