import java.util.*;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private ArrayList<Room> rooms;
    private Stack<Room> path;
    private ArrayList<Item> itemsHeld;
    private HashMap<String, Item> SetItems;
    private int weightTotal;
            
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        parser = new Parser();
        path = new Stack<Room>();
        rooms = new ArrayList<Room>();
        itemsHeld = new ArrayList<Item>();
        SetItems = new HashMap<String, Item>();
        createRooms();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;
        Item glass, drink, food, money, diamond;
        Character Kolling;
        
        //Create the characters
        Kolling = new Character("Kolling", "Good man");
        
        //Create the items
        glass = new Item("glass", true , 10);
        SetItems.put("glass", glass);
        drink = new Item("drink", false , 3);
        SetItems.put("drink", drink);
        food = new Item("food", true , 5);
        SetItems.put("food", food);
        money = new Item("money", true , 1);
        SetItems.put("money", money);
        diamond = new Item("diamond", false , 100);
        SetItems.put("diamond", diamond);
        
        
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        rooms.add(outside);
        rooms.add(theater);
        rooms.add(pub);
        rooms.add(lab);
        rooms.add(office);
        
        //initialise room characters
        outside.addCharacter(Kolling);
        Kolling.addItem(glass);
        
        
        //initialise room items
        outside.addItem(glass);
        pub.addItem(drink);
        pub.addItem(food);
        theater.addItem(diamond);
        office.addItem(money);
        
        
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theater.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            for(Room room:rooms){
                room.moveCharacters();
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        printRoomInfo();
        System.out.println();
    }
    
    
    /**
     * Prints out the status of the player and location
     * 
     */
    private void printRoomInfo(){
        
        System.out.println(currentRoom.getLongDescription());
        System.out.println("Equip Load "+ getWeightTotal()+"/200");
        currentRoom.showAllItems();
        currentRoom.showAllNpcs();
    }
    
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("back")){
            backRoom();
        }
        else if (commandWord.equals("pickup")){
            pickUp(command);
        }
        else if (commandWord.equals("drop")){
            drop(command);
        }
        else if (commandWord.equals("inventory")){
            inventory();
        }
        else if (commandWord.equals("steal")){
            steal(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            path.add(currentRoom);
            currentRoom = nextRoom;
            printRoomInfo();
        }
    }
    
    /**
     * Try to go back to previous room.If there is a previous room,
     * goes to other room otherwise prints an error message
     * 
     */
    private void backRoom(){
        if(path.size() == 0){
            System.out.println("You can't go back anywhere....");
            return;
        }
        else{
            currentRoom = path.pop();
            printRoomInfo();
        }
    }
    
    
    private void drop(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }
    
        String object = command.getSecondWord();
        Item itemToDrop = SetItems.get(object);
    
        
        // If itemToDrop was found, remove it from itemsHeld and add it to the current room
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
     * Try to pick up items within a room. If there is an item picks it up
     * otherwise prints an error message
     * 
     */
    
    private void pickUp(Command command){
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("pickup what?");
            return;
        }
        
        Item itemToPickUp = null;
        
        String object = command.getSecondWord();
        ArrayList<Item> items = currentRoom.getItemList();
        
        // Try to find an item is in the room
        for(Item item: items){
            if(item.getDescription().equals(object)){
                itemToPickUp = item;
                ValidPickUp(itemToPickUp);
                break;
            }
        }
        
        if (itemToPickUp != null) {
            currentRoom.removeItem(itemToPickUp);
            } 
        else
        {
            System.out.println("That item isn't in the room");
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     *Returns weight total
     */
    private int getWeightTotal(){
        return weightTotal;
    }
    
    /**
     * 
     *@Returns a string determining if the user can pick up that item
     */
    
    private void ValidPickUp(Item item){
        if(weightTotal + item.getWeight() > 200)
            {
                System.out.println("You're carrying too much");
            }
        else if(!item.getPickedUp())
            {
                System.out.println("This item can't be picked up");
            }
        else
            {
                System.out.println("Item was picked up");
                itemsHeld.add(item);
                weightTotal += item.getWeight();
            }
        }
        
    /**
     * 
     *@Return a string showing all items held by user
     */
    private void inventory(){
        if(itemsHeld.size() > 0){
            for(Item item: itemsHeld){
                System.out.println(item.getLongDescription());
            }
        }
        else{
            System.out.println("You have no items with you");
        }
    }
    
    /**
     * 
     *@Return removes an item from an npc and adds it to the user
     */
    private void steal(Command command){
        if(!command.hasSecondWord() || !command.hasthirdWord()){
            System.out.println("Specify what and who you're stealing from");
            return;
        }
        Character Target = null;
        String Person = command.getSecondWord();
        ArrayList<Character> List = currentRoom.getCharacterList();
        for(Character npc:List){
            if(npc.getDescription().equals(Person)){
                Target = npc;
                break;
            }
        }
        if(Target == null){
            System.out.println("Can't steal from someone whose not here");
            return;
        }
        String Object = command.getThirdWord();
        Item sobject = null; //stolen object
        ArrayList<Item> List2 = Target.getItemHeld();
        for(Item item: List2){
            if(item.getDescription().equals(Object)){
                sobject = item;
                break;
            }
        }
        
        if(sobject == null){
            System.out.println("It doesn't have that item");
        }
        else{
            System.out.println("You have stolen that item");
            Target.getRidOf(sobject);
            itemsHeld.add(sobject);
        }
    }
}
