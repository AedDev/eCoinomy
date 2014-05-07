package de.mdstv.spigot.ecoinomy;

import de.mdstv.spigot.ecoinomy.account.Account;
import de.mdstv.spigot.ecoinomy.commands.AccountingCommand;
import de.mdstv.spigot.ecoinomy.commands.EcoinomyCommand;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * eCoinomy main class
 * @author Morph <admin@mds-tv.de>
 * @since 1.0.0
 */
public class Ecoinomy extends JavaPlugin {
    /**
     * Static accessor.
     */
    public static Ecoinomy plugin;
    
    /**
     * This file contains the accounts data.
     */
    private File accountsDatabase;
    
    /**
     * List of all accounts.
     */
    public HashMap<String, Account> accounts = new HashMap<>();
    
    /**
     * Plugin configuration.
     */
    private FileConfiguration config;
    
    /**
     * The base eCoinomy command object
     */
    private EcoinomyCommand baseEcoinomyCommand = new EcoinomyCommand();

    /**
     * Fired on Plugin startup.
     */
    @Override
    public void onEnable() {
        // Check if datafolder is existing
        // If not, it will be created
        if (!getDataFolder().isDirectory()) {
            getDataFolder().mkdir();
        }
        
        // Load config
        this.config = this.getConfig();
        
        // Get account database file name
        String fileName =
                this.config.getString("eCoinomy.saveFile", "accounts.dat");
        
        // Set accountsDatabase file
        this.accountsDatabase = new File(getDataFolder(), fileName);
        
        // Load accounts database, if file is existing.
        // Otherwise we'll have a blank database.
        try {
            this.loadAccountData();
        } catch (FileNotFoundException ex) {
            getLogger().log(Level.WARNING, "Could not find {0}",
                    accountsDatabase.getName());
        }
        
        // Set public accessor
        plugin = this;
        
        // Register commands
        this.setupCommands();
    }
    
    /**
     * Registers all eCoinomy Commands
     */
    private void setupCommands() {
        // Register base command
        this.getCommand("ec").setExecutor(this.baseEcoinomyCommand);
        
        // Register subcommands
        this.baseEcoinomyCommand.registerCommand(new AccountingCommand());
    }

    /**
     * Fired on Plugin shutdown
     */
    @Override
    public void onDisable() {
        this.saveAccountData();
    }
    
    /**
     * Registers a new Account.
     * @param account eCoinomy Account to register.
     * @throws NullPointerException If account is null
     */
    public void registerAccount(Account account) {
        // Check null
        if (account == null) {
            throw new NullPointerException("Account cannot be null");
        }
        
        // Put to list
        this.accounts.put(account.getName(), account);
    }
    
    /**
     * Saves the cached account data to default file location at
     * eCoinomy/accounts.dat.
     */
    public void saveAccountData() {
        this.saveAccountData(this.accountsDatabase);
    }
    
    /**
     * Saves the cached account data to given file location.
     * @param dest The destination for the accounts database.
     */
    public void saveAccountData(File dest) {
        // Check destination file
        if (!dest.isFile()) {
            // Try to create destination file
            try {
                dest.createNewFile();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, "Could not create database file", ex);
            }
        }
        
        // Try to save data
        try {
            // Init stream
            final FileOutputStream   fos = new FileOutputStream(dest);
            final ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Write data to file
            oos.writeObject(this.accounts);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException ex) {
            getLogger().log(Level.SEVERE, "Could not find account database at '"
                    + dest.getAbsolutePath() + "'", ex);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save data to '"
                    + dest.getAbsolutePath() + "'", ex);
        }
    }
    
    /**
     * Loads the account database from default file location at
     * eCoinomy/accounts.dat.
     * @throws FileNotFoundException If the database file could not be found.
     */
    public void loadAccountData() throws FileNotFoundException {
        this.loadAccountData(this.accountsDatabase);
    }
    
    /**
     * Loads the account database from given file location.
     * @param src Source file to read from.
     * @throws FileNotFoundException If the database file could not be found.
     */
    public void loadAccountData(File src) throws FileNotFoundException {
        // Check source file
        if (!src.isFile()) {
            throw new FileNotFoundException("Could not find database at '"
                    + src.getAbsolutePath() + "'");
        }
        
        try {
            // Init stream
            final FileInputStream   fis = new FileInputStream(src);
            final ObjectInputStream ois = new ObjectInputStream(fis);

            // Read accounts database from stream
            HashMap<String, Account> loadedAccounts =
                    (HashMap<String, Account>) ois.readObject();

            // Move accounts to global list
            this.accounts = loadedAccounts;

            // Close stream
            ois.close();
        } catch (FileNotFoundException ex) {
            getLogger().log(Level.SEVERE, "Could not find database at ''{0}''",
                    src.getAbsolutePath());
        } catch (ClassNotFoundException ex) {
            getLogger().log(Level.SEVERE, "Could not read database. It seems,"
                    + " that the file is corrupted or empty.");
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not read database at ''{0}''",
                    src.getAbsolutePath());
        }
    }
}
