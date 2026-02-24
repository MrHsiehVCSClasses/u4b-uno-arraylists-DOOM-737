package u6pp;

import java.util.ArrayList;

public class Uno{
    private ArrayList<Player> players;
    private CardStack deck;
    private CardStack discard;
    private int currentPlayerIndex;
    private boolean reversed;

    public Uno(ArrayList<Player> players, CardStack deck, CardStack discard, int currentPlayerIndex, boolean reversed){
        this.players = players;
        this.deck = deck;
        this.discard = discard;
        this.currentPlayerIndex = currentPlayerIndex;
        this.reversed = reversed;
    }

    //construcot for a new game
    public Uno(int numPlayers){
        players = new ArrayList<Player>();

        //make players
        for (int i=1; i<=numPlayers; i++){
            players.add(new Player("Player " + i));
        }

        deck = new CardStack();
        discard = new CardStack();
        currentPlayerIndex = 0;
        reversed = false;

        buildDeck();
        deck.shuffle();

        //deal 7 cards each
        for (int r =0; r<7; r++){
            for (Player p : players){
                drawCards(p, 1);
            }
        }

        //start discard with one card
        Card first = deck.pop();
        if (first == null) first = new Card(Card.RED, Card.ZERO);
        discard.push(first);
    }

    //returns players list
    public ArrayList<Player> getPlayers() {
        return players;
    }

    //returns current player
    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    //returns next player based on direction
    public Player getNextPlayer(){
        return players.get(nextIndex(currentPlayerIndex));
    }

    //returns the top discard card
    public Card getTopDiscard(){
        return discard.peek();
    }

    //returns winner if anyone has 0 cards
    public Player getWinner(){
        for(Player p : players){
            if(p.getHand().size() == 0 ) return p;
        }
        return null;
    }

    //builds a deck
    private void buildDeck(){
        String[] colors = {Card.RED, Card.GREEN, Card.BLUE, Card.YELLOW};
        String[] nums = {Card.ZERO, Card.ONE, Card.TWO, Card.THREE, Card.FOUR, Card.FIVE, Card.SIX, Card.SEVEN, Card.EIGHT, Card.NINE};

        for (String c : colors){
            for (String n : nums) {
                deck.push(new Card(c, n));
                deck.push(new Card(c, n));
            }

            deck.push(new Card(c, Card.SKIP));
            deck.push(new Card(c, Card.SKIP));

            deck.push(new Card(c, Card.REVERSE));
            deck.push(new Card(c, Card.REVERSE));

            deck.push(new Card(c, Card.DRAW_2));
            deck.push(new Card(c, Card.DRAW_2));
        }

        for (int i = 0; i<4; i++) deck.push(new Card(Card.WILD, Card.WILD));
        for (int i = 0; i<4; i++) deck.push(new Card(Card.WILD, Card.WILD_DRAW_4));
    }

    //gets the next index depending on reversed
    private int nextIndex(int inx){
        int n = players.size();
        if(!reversed) return (inx + 1) % n;
        return (inx - 1 + n) % n;
    }

    //moves current players forward by steps
    private void advanceTurn(int steps){
        for (int i = 0; i< steps; i++){
            currentPlayerIndex = nextIndex(currentPlayerIndex);
        }
    }

    //checks if a card should be treated as wild/wild draw 4
    private boolean isWildType(Card c){
        if(c == null) return false;
        return Card.WILD.equals(c.getValue()) || Card.WILD_DRAW_4.equals(c.getValue()) || Card.WILD.equals(c.getColor()) || Card.WILD_DRAW_4.equals(c.getColor());
    }

    //figures out what value the card is acting like
    private String effectiveValue(Card c){
        if(c==null) return null;
        if(Card.WILD.equals(c.getValue()) || Card.WILD_DRAW_4.equals(c.getValue())) return c.getValue();
        if(Card.WILD.equals(c.getColor()) || Card.WILD_DRAW_4.equals(c.getColor())) return c.getColor();
        return c.getValue();
    }

    //draws num cards, recycling discard into deck when deck is low
    private void drawCards(Player p, int num){
        if (p == null || num <= 0) return;

        for (int i = 0; i<num; i++){
            if (deck.getSize() <= 1){
                recycleDiscardIntoDeck();
                deck.shuffle();
            }

            Card drawn = deck.pop();
            if (drawn == null){
                recycleDiscardIntoDeck();
                drawn = deck.pop();
            }

            if(drawn != null){
                p.getHand().add(drawn);
            }
        }
    }

    //keeps top discard, moves rest into deck, shuffles deck
    private void recycleDiscardIntoDeck(){
        if(discard.getSize()<= 1) return;

        Card top = discard.pop();

        CardStack temp = new CardStack();
        while (!discard.isEmpty()){
            temp.push(discard.pop());
        }

        discard.push(top);

        while (!temp.isEmpty()){
            deck.push(temp.pop());
        }

        deck.shuffle();
    }

    //plays a card or draws if cardToPlay is null
    public boolean playCard(Card cardToPlay, String chosenColor){

        if(cardToPlay == null){
            if(deck.getSize() <= 1){
                recycleDiscardIntoDeck();
                
            }
            drawCards(getCurrentPlayer(), 1);
            advanceTurn(1);
            return true;
        }

        Card top = getTopDiscard();
        boolean canPlay = cardToPlay.canPlayOn(top);

        //mustt be in current player's hand
        int handIndex = -1;
        for(int i = 0; i < getCurrentPlayer().getHand().size(); i++){
            if(getCurrentPlayer().getHand().get(i) == cardToPlay){
                handIndex = i;
                break;
            }
        }
        if (handIndex == -1) return false;

        if (!canPlay) return false;
        

        

        

        //wild must set chosen color
        if(isWildType(cardToPlay)){
            boolean ok = cardToPlay.trySetColor(chosenColor);
            if (!ok) return false;
        }

        //remove from hand and place on discard
        getCurrentPlayer().getHand().remove(handIndex);
        discard.push(cardToPlay);

        String v = effectiveValue(cardToPlay);

        //reverse flips direction
        if(Card.REVERSE.equals(v)){
            reversed = !reversed;
            advanceTurn(1);
            return true;
        }

        //skips one player
        if(Card.SKIP.equals(v)){
            advanceTurn(2);
            return true;
        }

        //draw 2 makes next player draw 2 and lose turn
        if(Card.DRAW_2.equals(v)){
            Player target = getNextPlayer();
            drawCards(target, 2);
            advanceTurn(2);
            return true;
        }

        //wild draw 4 makes next player draw 4 and lose turn
        if(Card.WILD_DRAW_4.equals(v)){
            Player target = getNextPlayer();
            drawCards(target, 4);
            advanceTurn(2);
            return true;
        }

        //normal card just ends the turn
        advanceTurn(1);
        return true;





    }

    
    









}