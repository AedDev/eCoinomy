package de.mdstv.bukkit.ecoinomy.account;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Holds the perms for an AccountMember. This class is reusable for
 * multiple accounts.
 * @author Morph <admin@mds-tv.de>
 */
public class AccountPermissionSet implements Serializable {
    /**
     * The owner flag. If a user has the owner role, he can do anything with
     * the account in which he is member too.
     */
    private boolean isOwner = false;
    
    /**
     * The permission nodes. Every node can get a custom value.
     */
    private HashMap<String, Object> perms = new HashMap<>();
    
    /**
     * Sets a permission for this account.
     * @param node eCoinomy permission node.
     * @param value The value of this Permission node.
     * @throws NullPointerException If node is null.
     * @throws IllegalArgumentException If node is empty.
     */
    public void setPermission(String node, Object value) {
        // Check for null
        if (node == null) {
            throw new NullPointerException("Permission cannot be null");
        }
        
        // Check node content
        if (node.isEmpty()) {
            throw new IllegalArgumentException("cannot add empty permission"
                    + " node");
        }
        
        // Add or override permission node with new values
        this.perms.put(node, value);
    }
    
    /**
     * Gets the permission value for given node.
     * @param node The permission node to request.
     * @return The value of the permission node.
     * @throws NullPointerException If node is null.
     * @throws IllegalArgumentException If node is empty.
     */
    public Object getPermission(String node) {
        // Check for null
        if (node == null) {
            throw new NullPointerException("Permission cannot be null");
        }
        
        // Check node content
        if (node.isEmpty()) {
            throw new IllegalArgumentException("cannot get empty permission"
                    + " node");
        }
        
        // Return node value, if existing
        if (this.perms.containsKey(node)) {
            return this.perms.get(node);
        } else {
            return null;
        }
    }
    
    /**
     * Returns an String Array of all perms for this AccountMember.
     * @return String Array of Permission nodes.
     */
    public String[] getPermissions() {
        return (String[]) this.perms.values().toArray();
    }
    
    /**
     * Removes a permission node.
     * @param node Node to remove.
     * @throws NullPointerException If node is null.
     * @throws IllegalArgumentException If node is empty.
     */
    public void removePermission(String node) {
        // Check for null
        if (node == null) {
            throw new NullPointerException("Permission cannot be null");
        }
        
        // Check node content
        if (node.isEmpty()) {
            throw new IllegalArgumentException("cannot get empty permission"
                    + " node");
        }
        
        // Remove permission node
        this.perms.remove(node);
    }
    
    /**
     * Gets the owner flag for this permissions set.
     * @return true if this is a owner account, false if not.
     */
    public boolean isOwner() {
        return this.isOwner;
    }
    
    /**
     * Sets the owner flag for this permissions set.
     * @param isOwner true to give owner perms, false to revoke.
     */
    public void setOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }
}
