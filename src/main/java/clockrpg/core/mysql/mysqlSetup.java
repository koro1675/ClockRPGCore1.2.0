package clockrpg.core.mysql;


import clockrpg.core.ClockRPGCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mysqlSetup {

    ClockRPGCore plugin = ClockRPGCore.getPlugin(ClockRPGCore.class);

    private Connection connection;
    public String host, database, username, password, table;
    public int port;

    public void mysqlSetup() {

        host = plugin.getConfig().getString("config.host");
        port = plugin.getConfig().getInt("config.port");
        database = plugin.getConfig().getString("config.database");
        username = plugin.getConfig().getString("config.username");
        password = "";
        table = plugin.getConfig().getString("config.table");

        Bukkit.getConsoleSender().sendMessage("host: " + plugin.getConfig().getString("config.host"));
        Bukkit.getConsoleSender().sendMessage("port: " + plugin.getConfig().getInt("config.port"));
        Bukkit.getConsoleSender().sendMessage("database: " + plugin.getConfig().getString("config.database"));
        Bukkit.getConsoleSender().sendMessage("username: " + plugin.getConfig().getString("config.username"));
        Bukkit.getConsoleSender().sendMessage("password: " + plugin.getConfig().getString("config.password"));
        Bukkit.getConsoleSender().sendMessage("table: " + plugin.getConfig().getString("config.table"));




        try {
            synchronized (this) {
                Bukkit.getConsoleSender().sendMessage("[ClockRPGMagics] " + ChatColor.GREEN + "MYSQL CONNETING");

                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(
                        DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
                                this.username, this.password));

                Bukkit.getConsoleSender().sendMessage("[ClockRPGMagics] " + ChatColor.GREEN + database + "." + table + "CONNECTED");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
