package de.levin.antiend.command;

import com.google.inject.Inject;
import de.levin.antiend.AntiEnd;
import de.levin.antiend.data.Result;
import de.levin.antiend.data.repository.MessagesRepository;
import de.levin.antiend.flyingtext.IFlyingText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class CommandAntiEnd implements CommandExecutor, TabCompleter {

    private final MessagesRepository messages;
    private final IFlyingText flyingText;

    private static final String HELP_ARGUMENT = "help";
    private static final String RELOAD_ARGUMENT = "reload";

    private static final String HOLOGRAM_ARGUMENT = "hologram";
    private static final String CREATE_ARGUMENT = "create";
    private static final String DELETE_ARGUMENT = "delete";
    private static final String REMOVE_ARGUMENT = "remove";
    private static final String TELEPORT_ARGUMENT = "teleport";

    private static final String USE_PERMISSION = "antiend.use";
    private static final String ALL_PERMISSIONS = "antiend.*";

    @Inject
    public CommandAntiEnd(MessagesRepository messages, IFlyingText flyingText) {
        this.messages = messages;
        this.flyingText = flyingText;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission(USE_PERMISSION) == false || sender.hasPermission(ALL_PERMISSIONS) == false) {
            sendMessage(sender, messages.getNoPerms());
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase(HELP_ARGUMENT)) {
                List<String> HelpList = messages.getHelpList();
                for (String string : HelpList)
                    sender.sendMessage(string.replace("%p%", messages.getPrefix())
                            .replace("%v%", AntiEnd.getInstance().getDescription().getVersion())
                            .replace("&", "§"));

            } else if (args[0].equalsIgnoreCase(RELOAD_ARGUMENT)) {
                sendMessage(sender, messages.getReload());
                AntiEnd.getInstance().ReloadableStart(true);
            } else
                sendMessage(sender, messages.getHelp());

        } else if (args.length == 2)
            HandleHologramArgument(sender, args);
        else
            sendMessage(sender, messages.getHelp());

        return true;
    }

    private void HandleHologramArgument(CommandSender sender, String[] args) {
        if (sender instanceof Player == false) {
            sendMessage(sender, messages.getCommandCanNotBeExecutedInConsole());
            return;
        }

        if (args[0].equalsIgnoreCase(HOLOGRAM_ARGUMENT) == false) {
            sendMessage(sender, messages.getHelp());
            return;
        }

        var player = ((Player) sender);
        var argOne = args[1];

        if (argOne.equalsIgnoreCase(CREATE_ARGUMENT))
            if (flyingText.create(player.getLocation()) == Result.Success)
                sendMessage(sender, messages.getHologramCreatedSuccess());
            else if (flyingText.create(player.getLocation()) == Result.Failed)
                sendMessage(sender, messages.getHologramCreateFailed());
            else
                sendMessage(sender, messages.getHologramCreateError());


        if (argOne.equalsIgnoreCase(DELETE_ARGUMENT) || argOne.equalsIgnoreCase(REMOVE_ARGUMENT))
            if (flyingText.delete() == Result.Success)
                sendMessage(sender, messages.getHologramDeletedSuccess());
            else if (flyingText.delete() == Result.Failed)
                sendMessage(sender, messages.getHologramDeleteFailed());
            else
                sendMessage(sender, messages.getHologramDeleteError());

        if (argOne.equalsIgnoreCase(TELEPORT_ARGUMENT))
            if (flyingText.teleport(player.getLocation()) == Result.Success)
                sendMessage(sender, messages.getHologramTeleportedSuccess());
            else if (flyingText.teleport(player.getLocation()) == Result.Failed)
                sendMessage(sender, messages.getHologramTeleportFailed());
            else
                sendMessage(sender, messages.getHologramTeleportError());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        ArrayList<String> complete = new ArrayList<>();
        if (sender.hasPermission(USE_PERMISSION) == false || sender.hasPermission(ALL_PERMISSIONS) == false) {
            return complete;
        }

        if (args.length == 1) {
            complete.addAll(Arrays.asList(RELOAD_ARGUMENT, HELP_ARGUMENT, HOLOGRAM_ARGUMENT));
        }

        if (args.length == 2) {
            complete.addAll(Arrays.asList(CREATE_ARGUMENT, DELETE_ARGUMENT, TELEPORT_ARGUMENT));
        }

        return complete;

    }

    private void sendMessage(CommandSender sender, String message) {
        var replacedMessage = message.replace("%p%", messages.getPrefix());
        var coloredMessage = ChatColor.translateAlternateColorCodes('&', replacedMessage);

        sender.sendMessage(coloredMessage);
    }
}