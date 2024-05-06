package com.equqe.auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.postgresql.Driver;

/*
 * auth java plugin
 */
public class Plugin extends JavaPlugin
{
  public static Logger LOGGER=Logger.getLogger("auth");
  private CommandHandler commandHandler;
  public Connection connection;
  public FileConfiguration config;
  
  public void onEnable()
  {
    loadConfig();
    LOGGER.info("auth enabled");

    commandHandler = new CommandHandler(connection);
    getCommand("reg").setExecutor(commandHandler);
    try {
      DriverManager.registerDriver(new Driver());
      connection = DriverManager.getConnection(
          "jdbc:postgresql://" + config.getString("database.ip") + ":" + config.getInt("database.port") + "/"
              + config.getString("database.database") + "?stringtype=unspecified",
          config.getString("database.username"), config.getString("database.password"));
          commandHandler = new CommandHandler(connection);
          getCommand("reg").setExecutor(commandHandler);
    } catch (SQLException e) {
      LOGGER.severe("failed to register postgres driver");
      e.printStackTrace();
    }
  }

  public void onDisable()
  {
    LOGGER.info("auth disabled");
  }

  public void loadConfig() {
    getConfig().options().copyDefaults(true);
    saveConfig();
    config = getConfig();
  }
}
