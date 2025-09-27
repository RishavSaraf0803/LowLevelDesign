package VisitorPattern;

import java.util.ArrayList;
import java.util.List;

// Element interface
interface Shape {
    void accept(Visitor visitor);
    double getArea();
    double getPerimeter();
}

// Concrete Elements
class Circle implements Shape {
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    public double getRadius() {
        return radius;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visitCircle(this);
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
}

class Rectangle implements Shape {
    private final double width;
    private final double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visitRectangle(this);
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }
}

class Triangle implements Shape {
    private final double side1;
    private final double side2;
    private final double side3;
    
    public Triangle(double side1, double side2, double side3) {
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }
    
    public double getSide1() { return side1; }
    public double getSide2() { return side2; }
    public double getSide3() { return side3; }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visitTriangle(this);
    }
    
    @Override
    public double getArea() {
        // Using Heron's formula
        double s = (side1 + side2 + side3) / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }
    
    @Override
    public double getPerimeter() {
        return side1 + side2 + side3;
    }
}

// Visitor interface
interface Visitor {
    void visitCircle(Circle circle);
    void visitRectangle(Rectangle rectangle);
    void visitTriangle(Triangle triangle);
}

// Concrete Visitors
class AreaCalculator implements Visitor {
    private double totalArea = 0;
    
    @Override
    public void visitCircle(Circle circle) {
        double area = circle.getArea();
        totalArea += area;
        System.out.println("Circle area: " + String.format("%.2f", area));
    }
    
    @Override
    public void visitRectangle(Rectangle rectangle) {
        double area = rectangle.getArea();
        totalArea += area;
        System.out.println("Rectangle area: " + String.format("%.2f", area));
    }
    
    @Override
    public void visitTriangle(Triangle triangle) {
        double area = triangle.getArea();
        totalArea += area;
        System.out.println("Triangle area: " + String.format("%.2f", area));
    }
    
    public double getTotalArea() {
        return totalArea;
    }
}

class PerimeterCalculator implements Visitor {
    private double totalPerimeter = 0;
    
    @Override
    public void visitCircle(Circle circle) {
        double perimeter = circle.getPerimeter();
        totalPerimeter += perimeter;
        System.out.println("Circle perimeter: " + String.format("%.2f", perimeter));
    }
    
    @Override
    public void visitRectangle(Rectangle rectangle) {
        double perimeter = rectangle.getPerimeter();
        totalPerimeter += perimeter;
        System.out.println("Rectangle perimeter: " + String.format("%.2f", perimeter));
    }
    
    @Override
    public void visitTriangle(Triangle triangle) {
        double perimeter = triangle.getPerimeter();
        totalPerimeter += perimeter;
        System.out.println("Triangle perimeter: " + String.format("%.2f", perimeter));
    }
    
    public double getTotalPerimeter() {
        return totalPerimeter;
    }
}

class ShapeInfoVisitor implements Visitor {
    
    @Override
    public void visitCircle(Circle circle) {
        System.out.println("Circle - Radius: " + circle.getRadius() + 
                          ", Area: " + String.format("%.2f", circle.getArea()) +
                          ", Perimeter: " + String.format("%.2f", circle.getPerimeter()));
    }
    
    @Override
    public void visitRectangle(Rectangle rectangle) {
        System.out.println("Rectangle - Width: " + rectangle.getWidth() + 
                          ", Height: " + rectangle.getHeight() +
                          ", Area: " + String.format("%.2f", rectangle.getArea()) +
                          ", Perimeter: " + String.format("%.2f", rectangle.getPerimeter()));
    }
    
    @Override
    public void visitTriangle(Triangle triangle) {
        System.out.println("Triangle - Sides: " + triangle.getSide1() + ", " + 
                          triangle.getSide2() + ", " + triangle.getSide3() +
                          ", Area: " + String.format("%.2f", triangle.getArea()) +
                          ", Perimeter: " + String.format("%.2f", triangle.getPerimeter()));
    }
}

// Object Structure
class ShapeCollection {
    private final List<Shape> shapes;
    
    public ShapeCollection() {
        this.shapes = new ArrayList<>();
    }
    
    public void addShape(Shape shape) {
        shapes.add(shape);
    }
    
    public void accept(Visitor visitor) {
        for (Shape shape : shapes) {
            shape.accept(visitor);
        }
    }
    
    public List<Shape> getShapes() {
        return new ArrayList<>(shapes);
    }
}

// Test class
class ShapeVisitorTest {
    public static void main(String[] args) {
        ShapeCollection collection = new ShapeCollection();
        
        // Add shapes
        collection.addShape(new Circle(5));
        collection.addShape(new Rectangle(4, 6));
        collection.addShape(new Triangle(3, 4, 5));
        collection.addShape(new Circle(3));
        
        System.out.println("=== Shape Information ===");
        ShapeInfoVisitor infoVisitor = new ShapeInfoVisitor();
        collection.accept(infoVisitor);
        
        System.out.println("\n=== Area Calculation ===");
        AreaCalculator areaCalculator = new AreaCalculator();
        collection.accept(areaCalculator);
        System.out.println("Total Area: " + String.format("%.2f", areaCalculator.getTotalArea()));
        
        System.out.println("\n=== Perimeter Calculation ===");
        PerimeterCalculator perimeterCalculator = new PerimeterCalculator();
        collection.accept(perimeterCalculator);
        System.out.println("Total Perimeter: " + String.format("%.2f", perimeterCalculator.getTotalPerimeter()));
    }
}
