package de.mdstv.bukkit.ecoinomy.account;

import de.mdstv.bukkit.ecoinomy.Ecoinomy;
import java.io.Serializable;
import org.bukkit.OfflinePlayer;

/**
 * Describes an AccountMember.
 * Every AccountMember can be added to multiple accounts.
 * @author Morph <admin@mds-tv.de>
 */
public class AccountMember implements Serializable {
    /**
     * Corresponding Player.
     */
    private String playerName;

    /**
     * Creates a new AccountMember from Bukkit Player.
     * @param playerName  The name of the Bukkit Player.
     */
    public AccountMember(String playerName) {
        this.playerName = playerName;
    }
    
    /**
     * Gets the corresponding Bukkit Player for this AccountMember.
     * The return value might be null, if the player could not be found.
     * @return The corresponding Bukkit Player.
     */
    public OfflinePlayer getPlayer() {
        return Ecoinomy.plugin.getServer().getOfflinePlayer(this.playerName);
    }

    /**
     * Gets the in-game name for this AccountMember
     * @return The in-game name
     */
    public String getName() {
        return this.playerName;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
