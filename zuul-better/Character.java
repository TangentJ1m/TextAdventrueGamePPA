import java.util.*;
/**
 * Class character - a character in an adventure game
 * 
 * This class is part of the "World of Zuul" application
 *
 * A Character represent an npc in the scenery in the game.
 * They travel across rooms with user interference
 * @author (Tanjim)
 * @version (07/11/2024)
 */
public class Character
{
    
    private String Description;
    private String Effect;
    private ArrayList<Item> itemsHeld;
    private boolean moved;
    

    /**
     * Constructor for objects of class Character
     */
    public Character(String Description, String Effect)
    {
        // initialise instance variables
        this.Description = Description;
        this.Effect = Effect;
        itemsHeld = new ArrayList<Item>();
        moved = false;
    }

    /**
     * Adds itemheld to character
     *
     * @param  item - item given to character
     */
    public void addItem(Item item){
        itemsHeld.add(item);
    }
    
    /**
     * 
     * @returns description
     */
    public String getDescription(){
        return Description;
    }
    
    /**
     * 
     *@Returns the list of items the character holds
     */
    public ArrayList<Item> getItemHeld(){
        return itemsHeld;
    }
    
    public void getRidOf(Item item){
        itemsHeld.remove(item);
    }
    public void Moved(){
        moved = true;
    }
    public boolean getMoved(){
        return moved == true;
    }
    public void NotMoved(){
        moved = false;
    }
    }

