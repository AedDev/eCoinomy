package de.mdstv.spigot.ecoinomy.commands;

import de.mdstv.spigot.ecoinomy.Ecoinomy;
import de.mdstv.spigot.ecoinomy.account.Account;
import de.mdstv.spigot.ecoinomy.account.AccountMember;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * eCoinomy accounting sub-command
 * @author Morph <admin@mds-tv.de>
 */
public class AccountingCommand implements EcoinomyCommandBase {
    /**
     * Gets the name of the accounting subcommand
     * @return Subcommand name
     */
    @Override
    public String getName() {
        return "account";
    }
    
    /**
     * Executes the accounting subcommand
     * @param sender The command sender
     * @param cmd The command object
     * @param label The name (label) of the base command
     * @param args The subcommand arguments
     * @return true on success, false on failure
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If there are no args or first argument is 'help' show help message
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            ArrayList<String> helpMessage = new ArrayList<>();
            
            // Build message
            // TODO Outsource help messages
            helpMessage.add("== eCoinomy Help - Accounting ==");
            helpMessage.add("&9/ec account create &7<&enew_name&7> <&enew_owner&7> - Create new account");
            helpMessage.add("&9/ec account remove &7<&eaccount&7> - Remove an account");
            helpMessage.add("&9/ec account list&7 - Shows a list of all accounts");
            helpMessage.add("&9/ec account rename &7<&eaccount&7> <&enew_name&7> - Renames an existing account");
            helpMessage.add("&9/ec account member &7<&eaccount&7> <&eadd&7|&edel&7|&elist&7> <&eplayer&7> - Adds or removes a member");
            helpMessage.add("&9/ec account perm &7<&eaccount&7> <&emember&7> <&eset&7|&edel&7> <&enode&7> [&bvalue&7] - Sets or removes a permission");
            
            // Replace color codes
            for (int i = 0; i < helpMessage.size(); i++) {
                String raw        = helpMessage.get(i);
                String translated = ChatColor.translateAlternateColorCodes('&', raw);
                helpMessage.set(i, translated);
            }
            
            // Convert to native String Array
            String[] helpMessageArr = new String[helpMessage.size()];
            helpMessage.toArray(helpMessageArr);
            
            // Send the message to command sender
            sender.sendMessage(helpMessageArr);
            
            return true;
        }
        
        // There are more than 0 arguments and the first argument was not 'help'
        // So store the first argument as subCommand
        String   subCommand     = args[0];
        String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
        
        // Switches the subCommand
        // TODO Outsource subcommand to seperate classes
        switch (subCommand) {
            case "list": {
                sender.sendMessage("== eCoinomy Accounts List ==");
                
                if (Ecoinomy.plugin.accounts.values().isEmpty()) {
                    sender.sendMessage(ChatColor.ITALIC + "There are no accounts");
                    return true;
                }
                
                // Iterate accounts
                int accCounter = 1;
                for (Account acc : Ecoinomy.plugin.accounts.values()) {
                    // Message string
                    String msg = "&9%d. %s &7(Balance: &e%s&7, Owners: &e%s&7)";
                    String colorizedMsg = ChatColor.translateAlternateColorCodes('&', msg);
                    
                    // Send formatted message
                    sender.sendMessage(String.format(colorizedMsg, accCounter++, acc.getName(), acc.getBalance(), Arrays.toString(acc.getOwners().toArray())));
                }
                
                return true;
            }
            case "create": {
                // To create an account, we need the player and the name for the
                // new account
                if (subCommandArgs.length >= 2) {
                    String accName  = subCommandArgs[0];
                    String accOwner = subCommandArgs[1];
                    
                    // Execute sub-command to create the account
                    return this.createAccount(sender, accName, accOwner);
                } else {
                    sender.sendMessage(ChatColor.RED + "[eCoinomy] You need to specify an account name and owner");
                    return true;
                }
            }
            case "remove": {
                // To remove an account, we need the name of these
                if (subCommandArgs.length >= 1) {
                    String accName = subCommandArgs[0];
                    
                    // Execute sub-command to remove the account
                    return this.removeAccount(sender, accName);
                } else {
                    sender.sendMessage(ChatColor.RED + "[eCoinomy] You need to specify the account name");
                    return true;
                }
            }
            case "rename": {
                // To rename an account, we need the curent name of these account
                // and the new name
                if (subCommandArgs.length >= 2) {
                    String curName = subCommandArgs[0];
                    String newName = subCommandArgs[1];
                    
                    // Do rename action
                    return this.renameAccount(sender, curName, newName);
                } else {
                    sender.sendMessage(ChatColor.RED + "[eCoinomy] You need to specify the current and new account name");
                    return true;
                }
            }
            case "member": {
                if (subCommandArgs.length >= 3) {
                    String account = subCommandArgs[0];
                    String action  = subCommandArgs[1];
                    String player  = subCommandArgs[2];
                    
                    switch (action) {
                        case "add": {
                            return this.addMemberToAccount(sender, account, player, false);
                        }
                        case "del": {
                            
                            break;
                        }
                        case "list": {
                            // TODO Account Member info - show members and owners
                            // Note: The output has to be colorized for better readability
                            break;
                        }
                        default: {
                            sender.sendMessage(ChatColor.RED + "[eCoinomy] Invalid action! Valid values are 'add' or 'del'.");
                        }
                    }
                    
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "[eCoionomy] You need to specify the account, the action you want to take and the corresponding player");
                    return true;
                }
            }
            case "perm": {
                
            }
            default: {
                sender.sendMessage(ChatColor.RED + "[eCoinomy] Unknown Sub-Command");
                return true;
            }
        }
    }
        
    /**
     * Creates a new eCoinomy account with owner
     * @param accountName Name for this account
     * @param owner Player which will be the owner (all perms)
     * @return true if account creation was successfully, false if not
     */
    public boolean createAccount(CommandSender sender, String accountName, String owner) {
        // Check if the account is already existing
        if (this.isAccountExisting(accountName)) {
            sender.sendMessage(ChatColor.RED + "[eCoinomy] This account is already existing");
            return true;
        }
        
        // Create owner member and account
        final AccountMember ownerMember = new AccountMember(owner);
        final Account       account     = new Account(accountName, ownerMember);

        // Store account and save
        Ecoinomy.plugin.registerAccount(account);
        Ecoinomy.plugin.saveAccountData();        

        // Send success message to sender
        sender.sendMessage(ChatColor.GREEN + "[eCoinomy] Account successfully created");
                        
        // If the creator is not the owner, send info message to new owner
        if (!sender.getName().equalsIgnoreCase(owner)) {
            // Get the Player object for owner and send message
            Player newOwner = sender.getServer().getPlayer(owner);
            if (newOwner != null) {
                newOwner.sendMessage(ChatColor.GREEN
                        + "[eCoinomy] You are now owner of the account '" + accountName + "'");
            }
            // Player not found, send warning to creator
            else {
                sender.sendMessage(ChatColor.GOLD
                        + "[eCoinomy] Warning: Unknown player '" + owner + "'");
            }
        }

        // We're ready to go!
        return true;
    }
    
    /**
     * Checks if a account with the given name is existing
     * @param name Name of the account
     * @return true if the account is existing, false if not
     */
    public boolean isAccountExisting(String name) {
        return Ecoinomy.plugin.accounts.containsKey(name);
    }
    
    /**
     * Removes an account from database
     * @param sender The sender of the command
     * @param accName The name of the account to remove
     * @return true on success, false on failure
     */
    public boolean removeAccount(CommandSender sender, String accName) {
        // Remove account from database and get the response
        Object result = Ecoinomy.plugin.accounts.remove(accName);
        
        // If response is null, the account did not exist
        if (result == null) {
            sender.sendMessage(ChatColor.GOLD + "[eCoinomy] Unknown account");
        } else {
            sender.sendMessage(ChatColor.GREEN + "[eCoinomy] Account removed!");
        }
        
        // Save after this action!
        Ecoinomy.plugin.saveAccountData();
        
        return true;
    }
    
    /**
     * Change the name of an existing account
     * @param sender The sender of the command
     * @param curName The current name of the account
     * @param newName The new name for this account
     * @return true on success, false on failure
     */
    public boolean renameAccount(CommandSender sender, String curName, String newName) {
        // Remove the account from database and get the object for modification
        Account acc = Ecoinomy.plugin.accounts.remove(curName);
        
        // Rename the account
        acc.setName(newName);
        
        // Put the account back to accounts database
        Ecoinomy.plugin.accounts.put(newName, acc);
        
        sender.sendMessage(ChatColor.GREEN + "[eCoinomy] Account successfully renamed");
        
        // Save after this action!
        Ecoinomy.plugin.saveAccountData();
        
        // All went well!
        return true;
    }
    
    /**
     * Adds a player as new member to account
     * @param sender The sender of the command
     * @param account The account to add the player to
     * @param playerName The name of the player to add
     * @param asOwner If true, the player will be added as owner
     * @return true on success, false on failure
     */
    public boolean addMemberToAccount(CommandSender sender, String account, String playerName, boolean asOwner) {
        Account accObject = Ecoinomy.plugin.accounts.get(account);
        
        // Check, if the account object is not null (null means: Not existing)
        if (accObject != null) {
            // Create new AccountMember from player name
            AccountMember accMember = new AccountMember(playerName);
            
            // Add the new member
            accObject.addMember(accMember);
            accObject.getMemberPerms(playerName).setOwner(asOwner);
            
            // Save after this action!
            Ecoinomy.plugin.saveAccountData();
            
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "[eCoionomy] Unknown account '" + account + "'");
            return true;
        }
    }
}