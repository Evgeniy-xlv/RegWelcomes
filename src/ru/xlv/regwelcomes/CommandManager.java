package ru.xlv.regwelcomes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Xlv on 06.09.2017.
 */
public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("regwelcomes.reload")) {
            commandSender.sendMessage(RegWelcomes.PREFIX + "You don't have permissions!");
            return true;
        }

        if(strings.length == 1) {
            if(strings[0].equalsIgnoreCase("reload")) {
                RegWelcomes.loadConfig();
                RegWelcomes.loadRegData();
                commandSender.sendMessage(RegWelcomes.PREFIX + "Configurations have been reloaded!");
                return true;
            }
        }

        commandSender.sendMessage("-------------------------");
        commandSender.sendMessage(RegWelcomes.PREFIX);
        commandSender.sendMessage("-------------------------");
        commandSender.sendMessage(ChatColor.YELLOW + "/rw reload - Reload plugin configs.");
        return true;
    }
}
