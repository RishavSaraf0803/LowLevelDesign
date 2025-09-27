package BuilderPattern;

import java.util.List;
import java.util.ArrayList;

// Fluent Builder with Method Chaining
public class PizzaBuilder {
    private String dough = "Regular";
    private String sauce = "Tomato";
    private String cheese = "Mozzarella";
    private List<String> toppings = new ArrayList<>();
    private String size = "Medium";
    private boolean extraCheese = false;
    private boolean extraSauce = false;
    
    public PizzaBuilder dough(String dough) {
        this.dough = dough;
        return this;
    }
    
    public PizzaBuilder sauce(String sauce) {
        this.sauce = sauce;
        return this;
    }
    
    public PizzaBuilder cheese(String cheese) {
        this.cheese = cheese;
        return this;
    }
    
    public PizzaBuilder addTopping(String topping) {
        this.toppings.add(topping);
        return this;
    }
    
    public PizzaBuilder size(String size) {
        this.size = size;
        return this;
    }
    
    public PizzaBuilder extraCheese() {
        this.extraCheese = true;
        return this;
    }
    
    public PizzaBuilder extraSauce() {
        this.extraSauce = true;
        return this;
    }
    
    public Pizza build() {
        return new Pizza(dough, sauce, cheese, toppings, size, extraCheese, extraSauce);
    }
}
