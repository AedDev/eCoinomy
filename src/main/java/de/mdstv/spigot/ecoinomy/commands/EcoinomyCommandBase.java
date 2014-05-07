package de.mdstv.spigot.ecoinomy.commands;

import org.bukkit.command.CommandExecutor;

/**
 * Extended CommandExecutor for eCoinomy subcommands
 * @author Morph <admin@mds-tv.de>
 */
public interface EcoinomyCommandBase extends CommandExecutor {
    /**
     * Gets the name of the subcommand
     * @return Subcommand name
     */
    public String getName();
}
