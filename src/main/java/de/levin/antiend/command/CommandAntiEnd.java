package de.levin.antiend.command;

import de.levin.antiend.AntiEnd;
import de.levin.antiend.other.Translation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CommandAntiEnd implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        FileConfiguration messages = Translation.getLanguageConfiguration();
        if (!sender.hasPermission("antiend.use") || !sender.hasPermission("antiend.*")) {
            sendMessage(sender, messages.getString("noPerms"), messages);
            return true;
        }
        if (args.length != 1) {
            sendMessage(sender, messages.getString("help"), messages);

        } else {
            if (args[0].equalsIgnoreCase("help")) {
                List<String> HelpList = messages.getStringList("helpList");
                for (String string : HelpList) {
                    sender.sendMessage(string.replace("%p%", messages.getString("prefix"))
                            .replace("%v%", AntiEnd.getInstance().getDescription().getVersion().replace("&", "§"))
                            .replace("&", "§"));
                }

            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    AntiEnd.reloadPlugin();
                    sendMessage(sender, messages.getString("reload"), messages);

                } else {
                    if (sender.hasPermission("antiend.use") | sender.hasPermission("antiend.*")) {
                        sendMessage(sender, messages.getString("help"), messages);

                    }
                }
            }

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> complete = new ArrayList<String>();
        if (args.length == 1 && sender.hasPermission("antiend.use") | sender.hasPermission("antiend.*")) {
            complete.add("reload");
            complete.add("help");
            return complete;
        }
        return null;

    }

    private void sendMessage(CommandSender sender, String message, FileConfiguration messages) {
        sender.sendMessage(message.replace("%p%", messages.getString("prefix")
                .replaceAll("&", "§")));
    }
}
