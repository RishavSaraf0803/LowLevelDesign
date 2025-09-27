package BuilderPattern;

import java.util.List;

public class Pizza {
    private final String dough;
    private final String sauce;
    private final String cheese;
    private final List<String> toppings;
    private final String size;
    private final boolean extraCheese;
    private final boolean extraSauce;
    
    public Pizza(String dough, String sauce, String cheese, List<String> toppings,
                 String size, boolean extraCheese, boolean extraSauce) {
        this.dough = dough;
        this.sauce = sauce;
        this.cheese = cheese;
        this.toppings = new java.util.ArrayList<>(toppings);
        this.size = size;
        this.extraCheese = extraCheese;
        this.extraSauce = extraSauce;
    }
    
    @Override
    public String toString() {
        return "Pizza{" +
                "dough='" + dough + '\'' +
                ", sauce='" + sauce + '\'' +
                ", cheese='" + cheese + '\'' +
                ", toppings=" + toppings +
                ", size='" + size + '\'' +
                ", extraCheese=" + extraCheese +
                ", extraSauce=" + extraSauce +
                '}';
    }
}
