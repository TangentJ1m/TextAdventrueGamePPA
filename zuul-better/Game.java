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
    private Player player1;
    private ArrayList<Room> rooms;
            
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        parser = new Parser();
        player1 = new Player();
        rooms = new ArrayList<Room>();
        createRooms();
        play();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, teleporter;
        Item glass, drink, food, money, diamond;
        Character Kolling;
        
        //Create the characters
        Kolling = new Character("Kolling", "Good man");
        
        //Create the items
        glass = new Item("glass", true , 10);
        //SetItems.put("glass", glass);
        drink = new Item("drink", false , 3);
        //SetItems.put("drink", drink);
        food = new Item("food", true , 5);
        //SetItems.put("food", food);
        money = new Item("money", true , 1);
        //SetItems.put("money", money);
        diamond = new Item("diamond", false , 100);
        //SetItems.put("diamond", diamond);
        
        
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        teleporter = new Room("Strange room. You shouldn't be here");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        rooms.add(outside);
        rooms.add(theater);
        rooms.add(pub);
        rooms.add(lab);
        rooms.add(office);
        rooms.add(teleporter);
        
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
        outside.setExit("North", teleporter);

        theater.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        player1.setRoom(outside); // start game outside
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
            if(player1.getRoom().getShortDescription().equals("Strange room. You shouldn't be here")){
                teleport(player1);
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    private void teleport(Player player){
        Random rand = new Random();
        Room randomRoom = rooms.get(rand.nextInt(rooms.size()));
        player1.setRoom(randomRoom);
        player1.clearPath();
        System.out.println("You have been teleported to a random room");
        printRoomInfo();
    }
    
    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Escape the labryinth. The aim of this game to find a way out whilst evading the minotaur.");
        System.out.print(" Escape the labryinth is a text-based game please type what you want to do.");
        System.out.println("Type 'help' if you need help.");
        printRoomInfo();
    }
    
    
    /**
     * Prints out the status of the player and location
     * 
     */
    private void printRoomInfo(){
        Room currentRoom = player1.getRoom();
        System.out.println(currentRoom.getLongDescription());
        currentRoom.showAllItems();
        currentRoom.showAllNpcs();
        System.out.println("Equip Load "+ player1.getWeight()+"/200");
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
        Room currentRoom = player1.getRoom();
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
            player1.setRoom(nextRoom);
            printRoomInfo();
        }
    }
    
    /**
     * Try to go back to previous room.If there is a previous room,
     * goes to other room otherwise prints an error message
     * 
     */
    private void backRoom(){
        if(player1.goBack() == true){
            printRoomInfo();
        }
        /**
        if(path.size() == 0){
            System.out.println("You can't go back anywhere....");
            return;
        }
        else{
            currentRoom = path.pop();
            printRoomInfo();
        }
        */
    }
    
    
    private void drop(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }
    
        String object = command.getSecondWord();
        player1.drop(object);
        //Item itemToDrop = SetItems.get(object);
    
        /**
        // If itemToDrop was found, remove it from itemsHeld and add it to the current room
        if (itemToDrop != null) {
            weightTotal -= itemToDrop.getWeight();
            itemsHeld.remove(itemToDrop);
            currentRoom.addItem(itemToDrop);
            System.out.println("Dropped " + itemToDrop.getDescription());
        } else {
            System.out.println("You don't have that item.");
        }
        */
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
        player1.pickUp(object);
        /**
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
        */
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
     * 
     *@Return a string showing all items held by user
     */
    private void inventory(){
        player1.inventory();
    }
    
    /**
     * 
     *@Return removes an item from an npc and adds it to the user
     *
     */
    private void steal(Command command){
        if(!command.hasSecondWord() || !command.hasthirdWord()){
            System.out.println("Specify what and who you're stealing from");
            return;
        }
        Character Target = null;
        String Person = command.getSecondWord();
        String StealItem = command.getThirdWord();
        player1.steal(Person, StealItem);
    }
}
