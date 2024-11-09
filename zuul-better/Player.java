import java.util.*;

/**
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 * 
 * This class creates the player class and stores information about each
 * player in the game
 *
 * @author Tanjim
 * @version 2024.08.11
 */

public class Player {

    private ArrayList<Item> itemsHeld;
    private int weightTotal;
    private Room currentRoom;
    private Stack<Room> path;

    /**
     * Constructor for objects of class Player
     */
    public Player() {
        path = new Stack<Room>();
        itemsHeld = new ArrayList<Item>();
    }

    /**
     * Sets the room the player is currently in
     */
    public void setRoom(Room room) {
        path.add(currentRoom);
        currentRoom = room;
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    /**
     * 
     * @Return returns the room of the player
     */
    public Room getRoom() {
        return currentRoom;
    }

    public void clearPath() {
        path = new Stack<Room>();
    }

    /**
     * 
     * @Return weight the room of the player
     */
    public int getWeight() {
        return weightTotal;
    }

    public void addItem(Item item) {
        itemsHeld.add(item);
    }

    /**
     * Try to go back to previous room. If there is a previous room,
     * goes to other room otherwise prints an error messge
     * 
     */
    public boolean goBack() {
        System.out.println(path.size());
        if (path.size() <= 1) {
            System.out.println("You can't go back anywhere....");
            return false;
        } else {
            currentRoom = path.pop();
            return true;
        }
    }

    /**
     * Try to drop an item specified by user. If item is held its dropped
     * otherwise it prints an error message
     */
    public void drop(String DroppedItem) {
        Item itemToDrop = null;

        for (Item item : itemsHeld) {
            if (item.isItem(DroppedItem)) {
                itemToDrop = item;
                break;
            }
        }

        if (itemToDrop != null) {
            weightTotal -= itemToDrop.getWeight();
            itemsHeld.remove(itemToDrop);
            currentRoom.addItem(itemToDrop);
            System.out.println("Dropped " + itemToDrop.getDescription());
        } else {
            System.out.println("You don't have that item.");
        }
    }

    /**
     * 
     * 
     */
    public void pickUp(String PickedupItem) {
        Item itemToPickUp = null;
        ArrayList<Item> items = currentRoom.getItemList();

        // Try to find an item is in the room
        for (Item item : items) {
            if (item.isItem(PickedupItem)) {
                itemToPickUp = item;
                ValidPickUp(itemToPickUp);
                break;
            }
        }

        if (itemToPickUp != null) {
            currentRoom.removeItem(itemToPickUp);
        } else {
            System.out.println("That item isn't in the room");
        }

    }

    /**
     * @Returns a string determining if the user can pick up that item
     * 
     */
    private void ValidPickUp(Item item) {
        if (weightTotal + item.getWeight() > 200) {
            System.out.println("You're carrying too much");
        } else if (!item.getPickedUp()) {
            System.out.println("This item can't be picked up");
        } else {
            System.out.println("Item was picked up");
            itemsHeld.add(item);
            weightTotal += item.getWeight();
        }
    }

    /**
     * 
     * @Return a string showing all items held by user
     */
    public void inventory() {
        if (itemsHeld.size() > 0) {
            for (Item item : itemsHeld) {
                System.out.println(item.getShortDescription());
            }
        } else {
            System.out.println("You have no items with you");
        }
    }

    /**
     * 
     */
    public void steal(String Target, String StealItem) {
        Character npc = null;
        npc = currentRoom.isCharacterHere(Target);
        if (npc == null) {
            System.out.println("Can't steal from someone whose not here");
            return;
        }
        Item StolenItem = null;
        StolenItem = npc.hasItemToSteal(StealItem);
        if (StolenItem != null) {
            itemsHeld.add(StolenItem);
            System.out.println("You have stolen " + StolenItem.getDescription());
        } else {
            System.out.println(Target + " doesn't have that on them");
        }
    }
}
