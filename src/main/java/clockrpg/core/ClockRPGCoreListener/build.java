package clockrpg.core.ClockRPGCoreListener;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class build implements Listener{


    //op以外のビルドを禁止
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if (!e.getPlayer().isOp()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if (!e.getPlayer().isOp()){
            e.setCancelled(true);
        }
    }

}
