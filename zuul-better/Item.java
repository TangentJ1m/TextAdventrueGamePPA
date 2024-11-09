
/**
 * Class Item - items in an adventure game. 
 * 
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * An "Item" represents one item in the scenery of the game.  It has it's
 * own weight and description.  For each existing item, it contains a bool
 * to store whether it can be picked up or not 
 * 
 * @author (Tanjim)
 * @version (07/11/24)
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String description;
    private boolean pickedUp;
    private int weight;

    /**
     * Constructor for objects of class item
     */
    public Item(String description, boolean pickedUp, int weight)
    {
        // initialise instance variables
        this.description = description;
        this.weight = weight;
        this.pickedUp = pickedUp;
        
    }

    /**
     *  Return a description of the item in the form:
     *     Glass Weight: 10 and can be picked up
     *     
     * @return A long description of item
     */
    public String getLongDescription()
    {
        // put your code here
        if(pickedUp == true)
        {
            return description + " Weight: " + weight + " and can be picked up";
        }
        else{
            return description + " Weight: " + weight + " and can't be picked up";
        }
    }

    public String getShortDescription()
    {
        return description + " Weight: " + weight;
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     * 
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * 
     * @return the weight of item
     * (the one that was defined in the constructor)
     */
    public int getWeight(){
        return weight;
    }
    
    /**
     * 
     * @returns whether you can pick up that item
     */
    public boolean getPickedUp(){
        return pickedUp;
    }
    
    /**
     * 
     */
    public boolean isItem(String name){
        return description.equals(name);
    }
}
