package u6pp;

public class Card {

    public static String RED = "RED";
    public static String GREEN = "GREEN";
    public static String BLUE = "BLUE";
    public static String YELLOW = "YELLOW";

    public static String ZERO = "0";
    public static String ONE = "1";
    public static String TWO = "2";
    public static String THREE = "3";
    public static String FOUR = "4";
    public static String FIVE = "5";
    public static String SIX = "6";
    public static String SEVEN = "7";
    public static String EIGHT = "8";
    public static String NINE = "9";

    public static String DRAW_2 = "DRAW_2";
    public static String REVERSE = "REVERSE";
    public static String SKIP = "SKIP";
    public static String WILD = "WILD";
    public static String WILD_DRAW_4 = "WILD_DRAW_4";

    // Wild color is the default color for wilds, before they are played. 
    public static String[] COLORS = {RED, GREEN, BLUE, YELLOW, WILD}; 
    public static String[] VALUES = {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, 
        DRAW_2, REVERSE, SKIP, WILD, WILD_DRAW_4};

    private String color;
    private String value;

    //creates a card with a color and value
    public Card(String color, String value){
        this.color = color;
        this.value = value;
    }

    //returns current color
    public String getColor(){
        return color;
    }

    //returns value
    public String getValue(){
        return value;
    }

    //checks if a string is inside an array
    private static boolean inArray(String s, String[] arr){
        if (s == null) return false;
        for (String x : arr){
            if (s.equals(x)) return true;
        }
        return false;
    }

    //true if his card is wild or wild draw 4
    private boolean isWildCard(){
        return WILD.equals(value) || WILD_DRAW_4.equals(value);
    }

    //only wild cards can change color, returns true if color changes
    public boolean trySetColor(String newColor){
        if (newColor == null) return false;
        if(!isWildCard()) return false;
        if(!inArray(newColor, COLORS)) return false;
        if(WILD.equals(newColor)) return false;

        color = newColor;
        return true;
    }

    public boolean canPlayOn(Card other){
        if (other == null) return false;

        if(isWildCard()) return true;
        
        if(WILD.equals(other.getColor())) return false;

        return this.color.equals(other.getColor()) || this.value.equals(other.getValue());
    }

    //cards are euals if their color and value are equal
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Card)) return false;
        Card other = (Card) o;
        return this.color.equals(other.color) && this.value.equals(other.value);
    }

    @Override
    public int hashCode(){
        return 31 * color.hashCode() + value.hashCode();
    }

    @Override
    public String toString(){
        return color + " " + value;
    }

}
