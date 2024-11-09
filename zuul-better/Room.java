import java.util.*;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game. It is
 * connected to other rooms via exits. For each existing exit, the room
 * stores a reference to the neighboring room.
 * 
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room {
    private String description;
    private HashMap<String, Room> exits; // stores exits of this room.
    private ArrayList<Item> items; // Stores the items of this room.
    private ArrayList<Character> npcs; // Stores the npcs of this room.

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description) {
        this.description = description;
        exits = new HashMap<>();
        items = new ArrayList<Item>();
        npcs = new ArrayList<Character>();
    }

    /**
     * Define an exit from this room.
     * 
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     *         (the one that was defined in the constructor).
     */
    public String getShortDescription() {
        return description;
    }

    /**
     * Return a description of the room in the form:
     * You are in the kitchen.
     * Exits: north west
     * 
     * @return A long description of this room
     */
    public String getLongDescription() {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Removes the item from the items array list for room instance
     * 
     */
    public void removeItem(Item item) {
        Iterator<Item> it = items.iterator();

        while (it.hasNext()) {
            Item Object = it.next();
            if (Object.getDescription().equals(item.getDescription())) {
                it.remove();
            }
        }
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * 
     * @return Details of the room's exits.
     */
    private String getExitString() {
        String returnString = "You can go:";
        Set<String> keys = exits.keySet();
        for (String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * 
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) {
        return exits.get(direction);
    }

    /**
     * Adds items to the room that can be used by either the user or
     * future characters
     * 
     * @param item The item added
     */
    public void addItem(Item item) {
        items.add(item);
    }

    public void moveCharacters() {

        // Create a list of exit directions to pick a random one
        List<String> exitDirections = new ArrayList<>(exits.keySet());

        // Move each character in the room to a random adjacent room
        for (Character npc : new ArrayList<>(npcs)) { // Iterate over a copy to avoid modification issues
            if (npc.getMoved() == false) {
                // Pick a random direction from the available exits
                String randomDirection = exitDirections.get(new Random().nextInt(exitDirections.size()));
                Room newRoom = exits.get(randomDirection);

                // Remove the character from the current room and add to the new room
                npcs.remove(npc);
                newRoom.addCharacter(npc);

                System.out.println(npc.getDescription() + " has moved to " + newRoom.getShortDescription() + " via "
                        + randomDirection + ".");
                npc.Moved();
            } else {
                npc.NotMoved();
            }
        }
    }

    /**
     * 
     * Adds characters to the room that can be used for interaction
     * 
     * @param chracter the character added
     */
    public void addCharacter(Character character) {
        npcs.add(character);
    }

    /**
     * 
     * 
     * @return returns the item array list of the room
     * 
     */
    public ArrayList<Item> getItemList() {
        return items;
    }

    public ArrayList<Character> getCharacterList() {
        return npcs;
    }

    /**
     * Returns a string listing all the room's characters, for example
     * "You see a corpse".
     * 
     * @return all item descriptions of items in room
     */
    public void showAllNpcs() {
        if (npcs.size() > 0) {
            System.out.println("People you can interact");
            for (Character npc : npcs) {
                System.out.println("!> " + npc.getDescription());
            }
        } else {
            System.out.println("There's no one here.....");
        }
    }

    /**
     * Returns a string listing all the room's items, for example
     * "Glass Weight: 12".
     * 
     * @return all item descriptions of items in room
     */
    public void showAllItems() {
        if (items.size() > 0) {
            System.out.println("Items here");
            for (Item item : items) {
                System.out.println("!> " + item.getLongDescription());
            }
        } else {
            System.out.println("There's no items here.....");
        }
    }

    public Character isCharacterHere(String Target) {
        for (Character npc : npcs) {
            if (npc.isNPC(Target)) {
                return npc;
            }
        }
        return null;
    }
}
