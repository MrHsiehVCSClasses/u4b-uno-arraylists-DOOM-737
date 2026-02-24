package u6pp;

import java.util.ArrayList;
import java.util.Random;

public class CardStack {
    private ArrayList<Card> cards;

    //creates empty stack
    public CardStack() {
        cards = new ArrayList<Card>();
    }

    //adds card to top of stack
    public void push(Card c) {
        cards.add(c);
    }

    //removes and returns top card
    public Card pop() {
        if (isEmpty()) return null;
        return cards.remove(cards.size() -1);
    }

    //returns top card withoug removing
    public Card peek() {
        if (isEmpty()) return null;
        return cards.get(cards.size() -1);
    }

    //checks if stack is empty
    public boolean isEmpty() {
        return cards.size() == 0;
    }

    //returns number of cards
    public int getSize() {
        return cards.size();
    }

    //removes all cardsd
    public void clear() {
        cards.clear();
    }

    //moves all cards from another stack into this one
    public void addAll(CardStack other) {
        if (other == null || this == other || other.isEmpty()) return;

        CardStack temp = new CardStack();
        
        while (!other.isEmpty()) temp. push(other.pop());
        while (!temp.isEmpty()) this.push(temp.pop());
    }

    //randomly shuffles cards
    public void shuffle(){
        Random rng = new Random();

        for (int i = cards.size() - 1; i>0; i--){
            int j = rng.nextInt(i + 1);
            Card t = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, t);
        }
    }
}
