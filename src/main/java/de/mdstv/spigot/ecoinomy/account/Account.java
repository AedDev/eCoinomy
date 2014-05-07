package de.mdstv.spigot.ecoinomy.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A bank account
 * @author Morph <admin@mds-tv.de>
 * @since 1.0.0
 */
public final class Account implements Serializable {
    /**
     * Members recognized to this Account.
     */
    private final HashMap<String, AccountMember> members = new HashMap<>();
    
    /**
     * Account member permissions.
     */
    private final HashMap<String, AccountPermissionSet> memberPerms =
            new HashMap<>();
    
    /**
     * Name of this account.
     */
    private String name = "<Unnamed>";
    
    /**
     * Contains the balance for this account.
     */
    private AccountBalance balance = new AccountBalance();

    /**
     * Creates a new Account with given Name
     * @param accountName Name for this account
     */
    public Account(String accountName) {
        this.name = accountName;
    }

    /**
     * Creates a new Account with given Name
     * @param accountName Name for this account
     * @param owner The owner for this account
     */
    public Account(String accountName, AccountMember owner) {
        this(accountName);
        
        // Add first member and set owner
        this.addMember(owner);
        this.getMemberPerms(owner.getName()).setOwner(true);
    }
    
    /**
     * Adds a new AccountMember to this bank account.
     * @param member The new member.
     * @throws NullPointerException If member is null.
     */
    public void addMember(AccountMember member) {
        // Check for null
        if (member == null) {
            throw new NullPointerException("Member cannot be null");
        }
        
        // Add the member to list
        this.members.put(member.getPlayer().getName(), member);
    }
    
    /**
     * Gets an AccountMember from this Account by its name. <code>null
     * </code> will returned, if the member is not associated with this account.
     * @param memberName The name of the member
     * @return The AccountMember object
     */
    public AccountMember getMemberByName(String memberName) {
        for (AccountMember member : this.members.values()) {
            if (member.getPlayer().getName().equalsIgnoreCase(memberName)) {
                return member;
            }
        }
        
        // Member not found
        return null;
    }
    
    /**
     * Removes a AccountMember from this Account.
     * @param member Member to remove.
     * @throws NullPointerException If member is null.
     */
    public void removeMember(AccountMember member) {
        this.removeMember(member.getPlayer().getName());
    }
    
    /**
     * Removes a AccountMember from this Account.
     * @param memberName The Bukkit Player name of the AccountMember.
     * @throws NullPointerException If memberName is null.
     * @throws IllegalArgumentException If memberName is empty.
     */
    public void removeMember(String memberName) {
        // Check null
        if (memberName == null) {
            throw new NullPointerException("The members name cannot be null");
        }
        
        // Check empty
        if (memberName.isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty");
        }
        
        // Remove (if existing, nothing will happen)
        this.members.remove(memberName);
    
    }
    
    /**
     * Sets the name for this account.
     * @param newName The new name for this account.
     */
    public void setName(String newName) {
        // Check for null
        if (newName == null) {
            throw new NullPointerException("Name cannnot be null");
        }
        
        this.name = newName;
    }
    
    /**
     * Returns the name of this account.
     * @return Name of this account.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Gets the AccountPermissionSet for given member.
     * @param memberName Name of the member
     * @return AccountPermissionSet for this member
     */
    public AccountPermissionSet getMemberPerms(String memberName) {
        // Get user perms
        AccountPermissionSet perms = this.memberPerms.get(memberName);
        
        // If the given user has no perms, create empty set
        if (perms == null) {
            AccountPermissionSet newPerms = new AccountPermissionSet();
            this.memberPerms.put(memberName, newPerms);
            
            // Return newly created permission set
            return this.memberPerms.get(memberName);
        }
        
        // The given member name has permissions, so return it
        else {
            return perms;
        }
    }
    
    /**
     * Returns a List of all AccountMembers which has no owner role.
     * @return All non-owner members for this account
     */
    public List<AccountMember> getMembers() {
        ArrayList<AccountMember> membersNoOwners = new ArrayList<>();
        
        // Filter all members and add all non-owner members to previously defined
        // ArrayList
        for (AccountMember member : this.members.values()) {
            if (!this.getMemberPerms(member.getName()).isOwner()) {
                membersNoOwners.add(member);
            }
        }
        
        // Return the non-owner members
        return membersNoOwners;
    }
    
    /**
     * Returns a List of all AccountMembers with owner role for this
     * account.
     * @return List of all AccountMembers with owner role
     */
    public List<AccountMember> getOwners() {
        ArrayList<AccountMember> owners = new ArrayList<>();
        
        // Filter all members and add all owners to previously defined ArrayList
        for (AccountMember member : this.members.values()) {
            if (this.getMemberPerms(member.getName()).isOwner()) {
                owners.add(member);
            }
        }
        
        // Return the owners
        return owners;
    }
    
    /**
     * Returns the AccountBalance for this Account.
     * @return Returns the AccountBalance for this Account
     */
    public AccountBalance getBalance() {
        return this.balance;
    }
}
