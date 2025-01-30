package de.levin.antiend.command;

import com.google.inject.Inject;
import de.levin.antiend.AntiEnd;
import de.levin.antiend.Data.MessagesRepository;
import lombok.var;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandAntiEnd implements CommandExecutor, TabCompleter {

    private final MessagesRepository messages;

    @Inject
    public CommandAntiEnd(MessagesRepository messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("antiend.use") || !sender.hasPermission("antiend.*")) {
            sendMessage(sender, messages.getNoPerms());
            return true;
        }
        if (args.length != 1) {
            sendMessage(sender, messages.getHelp());

        } else {
            if (args[0].equalsIgnoreCase("help")) {
                List<String> HelpList = messages.getHelpList();
                for (String string : HelpList)
                    sender.sendMessage(string.replace("%p%", messages.getPrefix())
                            .replace("%v%", AntiEnd.getInstance().getDescription().getVersion().replace("&", "§"))
                            .replace("&", "§"));

            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    AntiEnd.getInstance().ReloadableStart(true);
                    sendMessage(sender, messages.getReload());

                } else if (sender.hasPermission("antiend.use") | sender.hasPermission("antiend.*"))
                    sendMessage(sender, messages.getHelp());
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> complete = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("antiend.use") | sender.hasPermission("antiend.*")) {
            complete.add("reload");
            complete.add("help");
        }

        return complete;

    }

    private void sendMessage(CommandSender sender, String message) {
        var replacedMessage = message.replace("%p%", messages.getPrefix());
        var coloredMessage = ChatColor.translateAlternateColorCodes('&', replacedMessage);

        sender.sendMessage(coloredMessage);
    }
}
