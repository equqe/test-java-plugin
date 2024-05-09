package com.equqe.auth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor{
    private SQLHelper sqlHelper;
    
    public CommandHandler(Connection connection){
        sqlHelper = new SQLHelper(connection);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(command.getName().equalsIgnoreCase("reg") && sender instanceof Player){
            Player player = (Player) sender;
            String password = args[0];
            player.sendMessage("&aTrying to register your account...");
            
            // hashing the password to sha256
            password = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
            String updatePasswordSQL = "UPDATE player SET password = ? WHERE uid = ?";
            try {
                sqlHelper.update(updatePasswordSQL, password, player.getUniqueId().toString());
                player.sendMessage("&dYour account was successfully registered!");
            } catch (SQLException e) {
                player.sendMessage("&dFailed to register! Something is wrong with the database");
                e.printStackTrace();
            }

            return true;
        } else if(command.getName().equalsIgnoreCase("login") && sender instanceof Player){
            Player player = (Player) sender;
            String password = args[0];

            player.sendMessage("&aTrying to login into your account...");

            // hashing the password to sha256
            password = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);

            String checkLoginSQL = "SELECT * FROM player WHERE uid = ? AND password = ?";
            try {
                ResultSet result = sqlHelper.query(checkLoginSQL, player.getUniqueId().toString(), password);
                if (result.next()) {
                    player.sendMessage("&dSuccessfully logged in!");
                } else {
                    player.sendMessage("&cIncorrect password or account not found!");
                }
            } catch (SQLException e) {
                player.sendMessage("&dFailed to login! Something is wrong with the database");
                e.printStackTrace();
            } 
            
            return true;
        }
        return false;
    }
}
