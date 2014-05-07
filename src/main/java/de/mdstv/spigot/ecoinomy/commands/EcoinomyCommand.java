package de.mdstv.spigot.ecoinomy.commands;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * eCoinomy base command
 * @author Morph <admin@mds-tv.de>
 * @since 1.0.0
 */
public class EcoinomyCommand implements CommandExecutor {
    private ArrayList<EcoinomyCommandBase> subCommands = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If there are no args, or first argument is 'help', print help message
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            ArrayList<String> helpMessage = new ArrayList<>();
            
            // Build message
            // TODO Outsource help messages
            helpMessage.add("== eCoinomy Help ==");
            helpMessage.add("&9/ec&7 - shows this help screen");
            helpMessage.add("&9/ec account&7 - Account management");
            
            // Replace color codes
            for (int i = 0; i < helpMessage.size(); i++) {
                String raw        = helpMessage.get(i);
                String translated = ChatColor.translateAlternateColorCodes('&', raw);
                helpMessage.set(i, translated);
            }
            
            // Convert to simple array
            String[] msgArray = new String[helpMessage.size()];
            helpMessage.toArray(msgArray);
            
            // Send to CommandSender
            sender.sendMessage(msgArray);
            
            // Command was successfull
            return true;
        }
        
        // There are one or more arguments and the first argument was not 'help'
        // Store the first sub-command
        String subCommandName = args[0];
        
        // Check if subCommand is registered
        EcoinomyCommandBase subCommand = this.getSubcommandByName(subCommandName);
        if (subCommand != null) {
            // Execute the subcommand
            // First command argument is removed because due to is the name of
            // the subCommand.
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            return subCommand.onCommand(sender, cmd, label, subArgs);
        }
        
        // Unknown sub-command
        else {
            sender.sendMessage(ChatColor.RED + "[eCoinomy] Unknown subcommand");
            return true;
        }
    }
    
    /**
     * Registers a subcommand to the eCoinomy base command
     * @param cmd Command to register
     */
    public void registerCommand(EcoinomyCommandBase cmd) {
        // Check null
        if (cmd == null) {
            throw new NullPointerException("Command could not be null");
        }
        
        // Register command
        this.subCommands.add(cmd);
    }
    
    /**
     * Gets an eCoinomy subcommand by its name or null, if the
     * subcommand is not existing
     * @param cmdName Name of the subcommand
     * @return The subcommand
     */
    public EcoinomyCommandBase getSubcommandByName(String cmdName) {
        // Check null
        if (cmdName == null) {
            throw new NullPointerException("Command Name could not be null");
        }
        
        // Iterate subcommands array to search for given command name
        for (EcoinomyCommandBase cmd : this.subCommands) {
            if (cmd.getName().equalsIgnoreCase(cmdName)) {
                return cmd;
            }
        }
        
        return null;
    }
}
