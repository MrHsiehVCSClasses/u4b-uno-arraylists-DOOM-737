package u6pp;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> hand;

    //creates a player with a name and empty hand
    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<Card>();
    }

    //returns the player's name
    public String getName(){
        return name;
    }

    //returns the player's hand
    public ArrayList<Card> getHand(){
        return hand;
    }
}