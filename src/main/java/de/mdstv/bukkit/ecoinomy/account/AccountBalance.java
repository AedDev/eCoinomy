package de.mdstv.bukkit.ecoinomy.account;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Describes the balance of an eCoinomy Account.
 * @author Morph <admin@mds-tv.de>
 */
public class AccountBalance implements Serializable {
    public class Coin {
        /**
         * Name of the Coin in singular.
         */
        public String nameSingular;
        
        /**
         * Name of the Coin in plural.
         */
        public String namePlural;
        
        /**
         * Weight of this Coin.
         * 
         * For Example:
         * - Gold has a weight of 1
         * - Silver has a weight of 2
         * - Copper has a weight of 3
         * 
         * So you need x coins of copper to get one coin of silver.
         * Also you need x coins of silver to get one coin of gold.
         * 
         * What 'x' means you can see in the jDoc of maxStack.
         */
        public int weight;
        
        /**
         * The maxStack describes the maximum amount of Coins you can hold
         * before you get a 'next level' Coin.
         * 
         * For Example:
         * If were maxStack is 10, so you need 10 of this Coin type to get the
         * next lower weighted Coin. If the weight is already 1, you can hold
         * "unlimited" Coins of this type.
         */
        public int maxStack;
    }
    
    /**
     * Contains the amount of coins.
     */
    private HashMap<Coin, Integer> coins = new HashMap<>();
    
    /**
     * Gets the balance for given Coin name.
     * @param coinName The Coin name in singular.
     */
    public int getBalance(String coinName) {
        // Search for given coin name and return the amount, if found.
        for (Coin c : this.coins.keySet()) {
            if (c.nameSingular.equalsIgnoreCase(coinName)) {
                return this.coins.get(c);
            }
        }
        
        // If the given Coin name is unknown, throw this exception
        throw new IllegalArgumentException("Unknown Coin name given: '"
                + coinName + "'");
    }
    
    /**
     * Gets a String with all coins and amounts
     * @return Total amount of coins
     */
    public String getBalanceString() {
        StringBuilder coinsString = new StringBuilder();
        
        // TODO Coins weight comparator
        return null;
    }

    @Override
    public String toString() {
        return getBalanceString();
    }
}
