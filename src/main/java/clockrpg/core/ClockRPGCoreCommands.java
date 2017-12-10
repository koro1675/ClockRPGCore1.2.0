package clockrpg.core;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClockRPGCoreCommands implements CommandExecutor {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // 実行者がconsoleの場合
        if (sender instanceof Player == false){
            sender.sendMessage("This Command Can BE Only Executed By Player");
            return false;
        }

        Player player = (Player) sender;

        //引数がない場合
        if (args.length == 0){
            player.sendMessage(plugin.prefix + "§3§l/crpg [info, normalPlayer, redPlayer, blackPlayer, killCounter]");
            return false;
        }


        /*********************
         * version
         *********************/
        if (args[0].equalsIgnoreCase("ver")){
            player.sendMessage(plugin.prefix + "§3§lClockRPGCore var1.2.0");
            return false;
        }



        /*********************
         * PlayerInfo
         *********************/
        if (args[0].equalsIgnoreCase("info")){

            //引数が足りない場合
            if (args.length != 2){
                player.sendMessage(plugin.prefix + "§3§l/crpg info [player]");
                return false;
            }

            //権限の確認
            if (!player.hasPermission(plugin.clockRPGCorePermissions.adminPermisson)
                    || !player.hasPermission(plugin.clockRPGCorePermissions.commandPlayer)){
                player.sendMessage(plugin.prefix + "§4§lyou don't have permisssion");
                return false;
            }

            Player playerName = Bukkit.getPlayer(args[1]);
            plugin.clockRPGCorePlayer.playerInfo(player, playerName);
            return false;
        }


        /*********************
         * redPlayer
         *********************/
        if (args[0].equalsIgnoreCase("redPlayer")){

            //引数が足りない場合
            if (args.length != 3){
                player.sendMessage(plugin.prefix + "§3§l/crpg redPlayer set [playerName]");
                return false;
            }

            //権限の確認
            if (!player.hasPermission(plugin.clockRPGCorePermissions.adminPermisson)
                    || !player.hasPermission(plugin.clockRPGCorePermissions.commandPlayer)){
                player.sendMessage(plugin.prefix + "§4§lyou don't have permisssion");
                return false;
            }

            Player playerName = Bukkit.getPlayer(args[2]);

            //set処理
            if (args[1].equalsIgnoreCase("set")){
                plugin.clockRPGCorePlayer.setRedPlayer(playerName);
                return false;
            }
        }

        /*********************
         * blackPlayer
         *********************/
        if (args[0].equalsIgnoreCase("blackPlayer")){

            //引数が足りない場合
            if (args.length != 3){
                player.sendMessage(plugin.prefix + "§3§l/crpg blackPlayer set [playerName]");
                return false;
            }

            //権限の確認
            if (!player.hasPermission(plugin.clockRPGCorePermissions.adminPermisson)
                    || !player.hasPermission(plugin.clockRPGCorePermissions.commandPlayer)){
                player.sendMessage(plugin.prefix + "§4§lyou don't have permisssion");
                return false;
            }

            Player playerName = Bukkit.getPlayer(args[2]);


            //set処理
            if (args[1].equalsIgnoreCase("set")){
                plugin.clockRPGCorePlayer.setBlackPlayer(playerName);
                return false;
            }
        }


        /*********************
         * normalPlayer
         *********************/
        if (args[0].equalsIgnoreCase("normalPlayer")){

            //引数が足りない場合
            if (args.length != 3){
                player.sendMessage(plugin.prefix + "§3§l/crpg normalPlayer set [playerName]");
                return false;
            }

            //権限の確認
            if (!player.hasPermission(plugin.clockRPGCorePermissions.adminPermisson)
                    || !player.hasPermission(plugin.clockRPGCorePermissions.commandPlayer)){
                player.sendMessage(plugin.prefix + "§4§lyou don't have permisssion");
                return false;
            }

            Player playerName = Bukkit.getPlayer(args[2]);


            //set処理
            if (args[1].equalsIgnoreCase("set")){
                plugin.clockRPGCorePlayer.setNormalPlayer(playerName);
                return false;
            }
        }


        /*********************
         * killCounter
         *********************/
        if (args[0].equalsIgnoreCase("killCounter")){


            //権限の確認
            if (!player.hasPermission(plugin.clockRPGCorePermissions.adminPermisson)
                    || !player.hasPermission(plugin.clockRPGCorePermissions.commandKills)){
                player.sendMessage(plugin.prefix + "§4§lyou don't have permisssion");
                return false;
            }


            //引数が足りない場合
            if (args.length != 4){
                player.sendMessage(plugin.prefix + "§3§l/crpg killCounter set [playerName] [amount]");
                return false;
            }


            Player playerName = Bukkit.getPlayer(args[2]);
            Integer amount = Integer.parseInt(args[3]);

            //set処理
            if (args[1].equalsIgnoreCase("set")){

                //処理
                plugin.clockRPGCoreKillCounter.setKill(player, playerName, amount);
                return false;
            }
        }

        //何も当てはまらなかった場合のHelp
        player.sendMessage(plugin.prefix + "§3§l/crpg [info, normalPlayer, redPlayer, blackPlayer, killCounter]");
        return false;
    }
}
